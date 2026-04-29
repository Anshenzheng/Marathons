import { Component, OnInit } from '@angular/core';
import { EventService, Event, Announcement } from '../../services/event.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  upcomingEvents: Event[] = [];
  activeEvents: Event[] = [];
  announcements: Announcement[] = [];
  loading = true;

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.eventService.getActiveEvents().subscribe({
      next: (events) => {
        this.activeEvents = events;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });

    this.eventService.getUpcomingEvents().subscribe({
      next: (events) => {
        this.upcomingEvents = events.slice(0, 4);
      }
    });

    this.eventService.getPublishedAnnouncements().subscribe({
      next: (announcements) => {
        this.announcements = announcements.slice(0, 5);
      }
    });
  }

  formatDate(dateStr: string): { day: string; month: string } {
    const date = new Date(dateStr);
    const day = date.getDate().toString().padStart(2, '0');
    const months = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'];
    const month = months[date.getMonth()];
    return { day, month };
  }
}
