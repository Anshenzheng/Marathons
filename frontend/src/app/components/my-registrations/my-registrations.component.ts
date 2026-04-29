import { Component, OnInit } from '@angular/core';
import { RegistrationService, Registration } from '../../services/registration.service';

@Component({
  selector: 'app-my-registrations',
  templateUrl: './my-registrations.component.html',
  styleUrls: ['./my-registrations.component.css']
})
export class MyRegistrationsComponent implements OnInit {
  registrations: Registration[] = [];
  loading = true;

  constructor(private registrationService: RegistrationService) {}

  ngOnInit(): void {
    this.loadRegistrations();
  }

  loadRegistrations(): void {
    this.loading = true;
    
    this.registrationService.getMyRegistrations().subscribe({
      next: (registrations) => {
        this.registrations = registrations;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
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

  canCancel(registration: Registration): boolean {
    return registration.status === 'pending';
  }

  cancelRegistration(registration: Registration): void {
    if (!confirm('确定要取消报名吗？')) {
      return;
    }

    this.registrationService.cancelRegistration(registration.id).subscribe({
      next: () => {
        registration.status = 'cancelled';
        alert('取消报名成功');
      },
      error: (err) => {
        alert(err.error?.message || '取消报名失败');
      }
    });
  }
}
