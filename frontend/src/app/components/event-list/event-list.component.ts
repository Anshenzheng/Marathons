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
      case 'published': return 'bg-success';
      case 'offline': return 'bg-secondary';
      case 'ended': return 'bg-dark';
      default: return 'bg-secondary';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'published': return '报名中';
      case 'offline': return '已下架';
      case 'ended': return '已结束';
      default: return status;
    }
  }

  truncateText(text: string, maxLength: number): string {
    if (!text) return '';
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
  }
}
