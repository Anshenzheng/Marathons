import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RegistrationService, Registration, RegistrationStatistics, GroupStatistics } from '../../services/registration.service';
import { EventService, Event } from '../../services/event.service';

@Component({
  selector: 'app-admin-registration-list',
  templateUrl: './admin-registration-list.component.html',
  styleUrls: ['./admin-registration-list.component.css']
})
export class AdminRegistrationListComponent implements OnInit {
  eventId!: number;
  event: Event | null = null;
  registrations: Registration[] = [];
  statistics: RegistrationStatistics | null = null;
  loading = true;
  selectedIds: number[] = [];
  batchStatus: 'approved' | 'rejected' | null = null;
  batchRemark: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private registrationService: RegistrationService,
    private eventService: EventService
  ) {}

  ngOnInit(): void {
    this.eventId = Number(this.route.snapshot.paramMap.get('eventId'));
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    
    this.eventService.getEventById(this.eventId).subscribe({
      next: (event) => {
        this.event = event;
      }
    });

    this.registrationService.getEventRegistrations(this.eventId).subscribe({
      next: (registrations) => {
        this.registrations = registrations;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });

    this.registrationService.getRegistrationStatistics(this.eventId).subscribe({
      next: (stats) => {
        this.statistics = stats;
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'pending': return 'badge-pending';
      case 'approved': return 'badge-approved';
      case 'rejected': return 'badge-rejected';
      case 'cancelled': return 'badge-cancelled';
      default: return 'badge-secondary';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'pending': return '待审核';
      case 'approved': return '已通过';
      case 'rejected': return '已拒绝';
      case 'cancelled': return '已取消';
      default: return status;
    }
  }

  toggleSelection(id: number): void {
    const index = this.selectedIds.indexOf(id);
    if (index > -1) {
      this.selectedIds.splice(index, 1);
    } else {
      this.selectedIds.push(id);
    }
  }

  toggleAllSelection(): void {
    if (this.selectedIds.length === this.pendingRegistrations.length) {
      this.selectedIds = [];
    } else {
      this.selectedIds = this.pendingRegistrations.map(r => r.id);
    }
  }

  get pendingRegistrations(): Registration[] {
    return this.registrations.filter(r => r.status === 'pending');
  }

  reviewRegistration(registration: Registration, status: 'approved' | 'rejected'): void {
    const action = status === 'approved' ? '通过' : '拒绝';
    if (!confirm(`确定要${action}该报名吗？`)) {
      return;
    }

    const remark = prompt('请输入审核备注（可选）：', '');
    if (remark === null) return;

    this.registrationService.reviewRegistration(registration.id, status, remark).subscribe({
      next: () => {
        registration.status = status;
        alert(`审核${action}成功`);
        this.loadData();
      },
      error: (err) => {
        alert(err.error?.message || '审核失败');
      }
    });
  }

  batchReview(): void {
    if (this.selectedIds.length === 0) {
      alert('请先选择要审核的报名');
      return;
    }

    if (!this.batchStatus) {
      alert('请选择审核结果');
      return;
    }

    const action = this.batchStatus === 'approved' ? '通过' : '拒绝';
    if (!confirm(`确定要批量${action} ${this.selectedIds.length} 个报名吗？`)) {
      return;
    }

    this.registrationService.batchReview(this.selectedIds, this.batchStatus, this.batchRemark).subscribe({
      next: () => {
        alert(`批量审核${action}成功`);
        this.selectedIds = [];
        this.batchStatus = null;
        this.batchRemark = '';
        this.loadData();
      },
      error: (err) => {
        alert(err.error?.message || '批量审核失败');
      }
    });
  }

  exportData(): void {
    this.registrationService.exportEventRegistrations(this.eventId).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `报名名单_${new Date().toISOString().slice(0, 10)}.xlsx`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: () => {
        alert('导出失败');
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/admin/events']);
  }
}
