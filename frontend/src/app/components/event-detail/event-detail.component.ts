import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventService, Event, EventGroup, Regulation, PickupNote } from '../../services/event.service';
import { RegistrationService, RegistrationCreate } from '../../services/registration.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-event-detail',
  templateUrl: './event-detail.component.html',
  styleUrls: ['./event-detail.component.css']
})
export class EventDetailComponent implements OnInit {
  eventId!: number;
  event: Event | null = null;
  groups: EventGroup[] = [];
  regulation: Regulation | null = null;
  pickupNote: PickupNote | null = null;
  loading = true;
  submitting = false;
  selectedGroup: EventGroup | null = null;
  showRegisterModal = false;
  registerForm: FormGroup;
  isLoggedIn = false;
  activeTab: 'info' | 'groups' | 'regulation' | 'pickup' = 'info';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private eventService: EventService,
    private registrationService: RegistrationService,
    private authService: AuthService,
    private formBuilder: FormBuilder
  ) {
    this.registerForm = this.formBuilder.group({
      groupId: ['', Validators.required],
      shirtSize: ['', Validators.required],
      emergencyContact: [''],
      emergencyPhone: [''],
      medicalHistory: [''],
      remark: ['']
    });
  }

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.eventId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadEventData();
  }

  loadEventData(): void {
    this.loading = true;
    
    this.eventService.getEventById(this.eventId).subscribe({
      next: (event) => {
        this.event = event;
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

    this.eventService.getPublishedRegulation(this.eventId).subscribe({
      next: (regulation) => {
        this.regulation = regulation;
      }
    });

    this.eventService.getPublishedPickupNote(this.eventId).subscribe({
      next: (pickupNote) => {
        this.pickupNote = pickupNote;
      }
    });
  }

  openRegisterModal(group: EventGroup): void {
    if (!this.isLoggedIn) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: `/events/${this.eventId}` } });
      return;
    }
    
    this.selectedGroup = group;
    this.registerForm.patchValue({ groupId: group.id });
    this.showRegisterModal = true;
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }

    this.submitting = true;
    
    const formValue = this.registerForm.value;
    const registration: RegistrationCreate = {
      eventId: this.eventId,
      groupId: formValue.groupId,
      shirtSize: formValue.shirtSize,
      emergencyContact: formValue.emergencyContact,
      emergencyPhone: formValue.emergencyPhone,
      medicalHistory: formValue.medicalHistory,
      remark: formValue.remark
    };

    this.registrationService.createRegistration(registration).subscribe({
      next: () => {
        this.showRegisterModal = false;
        this.submitting = false;
        alert('报名成功！');
        this.router.navigate(['/my-registrations']);
      },
      error: (err) => {
        alert(err.error?.message || '报名失败，请稍后重试');
        this.submitting = false;
      }
    });
  }

  setActiveTab(tab: 'info' | 'groups' | 'regulation' | 'pickup'): void {
    this.activeTab = tab;
  }
}
