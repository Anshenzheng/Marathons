import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService, Announcement } from '../../services/event.service';

@Component({
  selector: 'app-announcement-detail',
  templateUrl: './announcement-detail.component.html',
  styleUrls: ['./announcement-detail.component.css']
})
export class AnnouncementDetailComponent implements OnInit {
  announcementId!: number;
  announcement: Announcement | null = null;
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private eventService: EventService
  ) {}

  ngOnInit(): void {
    this.announcementId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadAnnouncement();
  }

  loadAnnouncement(): void {
    this.loading = true;
    
    this.eventService.getAnnouncementById(this.announcementId).subscribe({
      next: (announcement) => {
        this.announcement = announcement;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}
