import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventService, Announcement } from '../../services/event.service';

@Component({
  selector: 'app-admin-announcement-form',
  templateUrl: './admin-announcement-form.component.html',
  styleUrls: ['./admin-announcement-form.component.css']
})
export class AdminAnnouncementFormComponent implements OnInit {
  announcementId: number | null = null;
  announcementForm: FormGroup;
  submitting = false;
  isEdit = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private eventService: EventService
  ) {
    this.announcementForm = this.formBuilder.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      eventId: [''],
      type: ['general'],
      isTop: [false]
    });
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.announcementId = Number(idParam);
      this.isEdit = true;
    }
  }

  onSubmit(): void {
    if (this.announcementForm.invalid) {
      return;
    }

    this.submitting = true;
    const formValue = this.announcementForm.value;

    const announcement: Partial<Announcement> = {
      title: formValue.title,
      content: formValue.content,
      eventId: formValue.eventId ? Number(formValue.eventId) : undefined,
      type: formValue.type,
      isTop: formValue.isTop ? 1 : 0,
      status: 'draft'
    };

    this.eventService.createAnnouncement(announcement).subscribe({
      next: () => {
        alert('公告创建成功');
        this.submitting = false;
        this.router.navigate(['/admin/events']);
      },
      error: (err) => {
        alert(err.error?.message || '创建失败');
        this.submitting = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/admin/events']);
  }
}
