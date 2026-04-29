import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventService, PickupNote } from '../../services/event.service';

@Component({
  selector: 'app-admin-pickup-note-form',
  templateUrl: './admin-pickup-note-form.component.html',
  styleUrls: ['./admin-pickup-note-form.component.css']
})
export class AdminPickupNoteFormComponent implements OnInit {
  eventId: number | null = null;
  pickupNoteForm: FormGroup;
  submitting = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private eventService: EventService
  ) {
    this.pickupNoteForm = this.formBuilder.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      pickupDate: [''],
      pickupLocation: ['']
    });
  }

  ngOnInit(): void {
    const eventIdParam = this.route.snapshot.paramMap.get('id');
    if (eventIdParam) {
      this.eventId = Number(eventIdParam);
    }
  }

  onSubmit(): void {
    if (this.pickupNoteForm.invalid) {
      return;
    }

    this.submitting = true;
    const formValue = this.pickupNoteForm.value;

    const pickupNote: Partial<PickupNote> = {
      eventId: this.eventId || 0,
      title: formValue.title,
      content: formValue.content,
      pickupDate: formValue.pickupDate || undefined,
      pickupLocation: formValue.pickupLocation || undefined,
      status: 'draft'
    };

    this.eventService.createPickupNote(pickupNote).subscribe({
      next: () => {
        alert('领物须知创建成功');
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
