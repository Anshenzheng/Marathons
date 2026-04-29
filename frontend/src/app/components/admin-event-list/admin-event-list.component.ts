import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EventService, Event } from '../../services/event.service';

@Component({
  selector: 'app-admin-event-list',
  templateUrl: './admin-event-list.component.html',
  styleUrls: ['./admin-event-list.component.css']
})
export class AdminEventListComponent implements OnInit {
  events: Event[] = [];
  loading = true;

  constructor(
    private eventService: EventService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.loading = true;
    
    this.eventService.getPublishedEvents().subscribe({
      next: (events) => {
        this.events = events;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'draft': return 'badge-draft';
      case 'published': return 'badge-published';
      case 'offline': return 'badge-offline';
      case 'ended': return 'badge-cancelled';
      default: return 'badge-secondary';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'draft': return '草稿';
      case 'published': return '已发布';
      case 'offline': return '已下架';
      case 'ended': return '已结束';
      default: return status;
    }
  }

  canPublish(event: Event): boolean {
    return event.status === 'draft';
  }

  canOffline(event: Event): boolean {
    return event.status === 'published';
  }

  editEvent(event: Event): void {
    this.router.navigate(['/admin/events/edit', event.id]);
  }

  viewRegistrations(event: Event): void {
    this.router.navigate(['/admin/registrations', event.id]);
  }

  publishEvent(event: Event): void {
    if (!confirm('确定要发布该赛事吗？')) {
      return;
    }

    this.eventService.publishEvent(event.id).subscribe({
      next: () => {
        event.status = 'published';
        alert('赛事发布成功');
      },
      error: (err) => {
        alert(err.error?.message || '发布失败');
      }
    });
  }

  offlineEvent(event: Event): void {
    if (!confirm('确定要下架该赛事吗？')) {
      return;
    }

    this.eventService.offlineEvent(event.id).subscribe({
      next: () => {
        event.status = 'offline';
        alert('赛事下架成功');
      },
      error: (err) => {
        alert(err.error?.message || '下架失败');
      }
    });
  }
}
