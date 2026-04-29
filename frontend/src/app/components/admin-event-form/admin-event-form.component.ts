import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventService, Event, EventGroup } from '../../services/event.service';

@Component({
  selector: 'app-admin-event-form',
  templateUrl: './admin-event-form.component.html',
  styleUrls: ['./admin-event-form.component.css']
})
export class AdminEventFormComponent implements OnInit {
  eventId: number | null = null;
  eventForm: FormGroup;
  groups: EventGroup[] = [];
  loading = false;
  submitting = false;
  isEdit = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private eventService: EventService
  ) {
    this.eventForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: [''],
      eventDate: ['', Validators.required],
      eventTime: [''],
      location: ['', Validators.required],
      registrationStartDate: ['', Validators.required],
      registrationEndDate: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.eventId = Number(idParam);
      this.isEdit = true;
      this.loadEvent();
    }
  }

  loadEvent(): void {
    if (!this.eventId) return;

    this.loading = true;
    this.eventService.getEventById(this.eventId).subscribe({
      next: (event) => {
        this.eventForm.patchValue({
          name: event.name,
          description: event.description,
          eventDate: event.eventDate,
          eventTime: event.eventTime,
          location: event.location,
          registrationStartDate: event.registrationStartDate,
          registrationEndDate: event.registrationEndDate
        });
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });

    this.eventService.getEventGroups(this.eventId).subscribe({
      next: (groups) => {
        this.groups = groups;
      }
    });
  }

  onSubmit(): void {
    if (this.eventForm.invalid) {
      return;
    }

    this.submitting = true;
    const formValue = this.eventForm.value;

    const eventData: Partial<Event> = {
      name: formValue.name,
      description: formValue.description,
      eventDate: formValue.eventDate,
      eventTime: formValue.eventTime,
      location: formValue.location,
      registrationStartDate: formValue.registrationStartDate,
      registrationEndDate: formValue.registrationEndDate
    };

    if (this.isEdit && this.eventId) {
      this.eventService.updateEvent(this.eventId, eventData).subscribe({
        next: () => {
          alert('赛事更新成功');
          this.submitting = false;
          this.router.navigate(['/admin/events']);
        },
        error: (err) => {
          alert(err.error?.message || '更新失败');
          this.submitting = false;
        }
      });
    } else {
      this.eventService.createEvent(eventData).subscribe({
        next: () => {
          alert('赛事创建成功');
          this.submitting = false;
          this.router.navigate(['/admin/events']);
        },
        error: (err) => {
          alert(err.error?.message || '创建失败');
          this.submitting = false;
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/admin/events']);
  }
}
