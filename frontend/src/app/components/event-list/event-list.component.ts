import { Component, OnInit } from '@angular/core';
import { EventService, Event } from '../../services/event.service';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.css']
})
export class EventListComponent implements OnInit {
  events: Event[] = [];
  loading = true;
  filter: 'all' | 'active' | 'upcoming' = 'all';

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.loading = true;
    
    let observable;
    if (this.filter === 'active') {
      observable = this.eventService.getActiveEvents();
    } else if (this.filter === 'upcoming') {
      observable = this.eventService.getUpcomingEvents();
    } else {
      observable = this.eventService.getPublishedEvents();
    }

    observable.subscribe({
      next: (events) => {
        this.events = events;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  onFilterChange(filter: 'all' | 'active' | 'upcoming'): void {
    this.filter = filter;
    this.loadEvents();
  }

  formatDate(dateStr: string): { day: string; month: string; year: string } {
    const date = new Date(dateStr);
    const day = date.getDate().toString().padStart(2, '0');
    const months = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'];
    const month = months[date.getMonth()];
    const year = date.getFullYear().toString();
    return { day, month, year };
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'active': return 'bg-success';
      case 'not_started': return 'bg-info';
      case 'ended': return 'bg-secondary';
      case 'offline': return 'bg-dark';
      case 'draft': return 'bg-warning';
      default: return 'bg-secondary';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'active': return '报名中';
      case 'not_started': return '未开始报名';
      case 'ended': return '已截止报名';
      case 'offline': return '已下架';
      case 'draft': return '草稿';
      default: return status;
    }
  }

  getRegistrationStatus(event: Event): string {
    if (event.registrationStatus) {
      return event.registrationStatus;
    }
    
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    const startDate = new Date(event.registrationStartDate);
    startDate.setHours(0, 0, 0, 0);
    
    const endDate = new Date(event.registrationEndDate);
    endDate.setHours(0, 0, 0, 0);
    
    if (event.status === 'offline') return 'offline';
    if (event.status === 'draft') return 'draft';
    
    if (today < startDate) return 'not_started';
    else if (today > endDate) return 'ended';
    else return 'active';
  }

  truncateText(text: string, maxLength: number): string {
    if (!text) return '';
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
  }
}
