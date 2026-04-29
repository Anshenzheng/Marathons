import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventService, Regulation } from '../../services/event.service';

@Component({
  selector: 'app-admin-regulation-form',
  templateUrl: './admin-regulation-form.component.html',
  styleUrls: ['./admin-regulation-form.component.css']
})
export class AdminRegulationFormComponent implements OnInit {
  eventId: number | null = null;
  regulationForm: FormGroup;
  submitting = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private eventService: EventService
  ) {
    this.regulationForm = this.formBuilder.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      version: [1]
    });
  }

  ngOnInit(): void {
    const eventIdParam = this.route.snapshot.paramMap.get('id');
    if (eventIdParam) {
      this.eventId = Number(eventIdParam);
    }
  }

  onSubmit(): void {
    if (this.regulationForm.invalid) {
      return;
    }

    this.submitting = true;
    const formValue = this.regulationForm.value;

    const regulation: Partial<Regulation> = {
      eventId: this.eventId || 0,
      title: formValue.title,
      content: formValue.content,
      version: formValue.version,
      status: 'draft'
    };

    this.eventService.createRegulation(regulation).subscribe({
      next: () => {
        alert('竞赛规程创建成功');
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
