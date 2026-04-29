import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { EventListComponent } from './components/event-list/event-list.component';
import { EventDetailComponent } from './components/event-detail/event-detail.component';
import { MyRegistrationsComponent } from './components/my-registrations/my-registrations.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { AdminEventListComponent } from './components/admin-event-list/admin-event-list.component';
import { AdminEventFormComponent } from './components/admin-event-form/admin-event-form.component';
import { AdminRegistrationListComponent } from './components/admin-registration-list/admin-registration-list.component';
import { AdminAnnouncementFormComponent } from './components/admin-announcement-form/admin-announcement-form.component';
import { AdminRegulationFormComponent } from './components/admin-regulation-form/admin-regulation-form.component';
import { AdminPickupNoteFormComponent } from './components/admin-pickup-note-form/admin-pickup-note-form.component';
import { AnnouncementDetailComponent } from './components/announcement-detail/announcement-detail.component';
import { NotFoundComponent } from './components/not-found/not-found.component';

import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';

const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'events', component: EventListComponent },
  { path: 'events/:id', component: EventDetailComponent },
  { path: 'announcements/:id', component: AnnouncementDetailComponent },
  { path: 'my-registrations', component: MyRegistrationsComponent, canActivate: [AuthGuard] },
  
  // 管理员路由
  { 
    path: 'admin', 
    component: AdminDashboardComponent, 
    canActivate: [AdminGuard],
    children: [
      { path: '', redirectTo: 'events', pathMatch: 'full' },
      { path: 'events', component: AdminEventListComponent },
      { path: 'events/create', component: AdminEventFormComponent },
      { path: 'events/edit/:id', component: AdminEventFormComponent },
      { path: 'registrations/:eventId', component: AdminRegistrationListComponent },
      { path: 'announcements/create', component: AdminAnnouncementFormComponent },
      { path: 'announcements/edit/:id', component: AdminAnnouncementFormComponent },
      { path: 'regulations/create', component: AdminRegulationFormComponent },
      { path: 'regulations/edit/:id', component: AdminRegulationFormComponent },
      { path: 'pickup-notes/create', component: AdminPickupNoteFormComponent },
      { path: 'pickup-notes/edit/:id', component: AdminPickupNoteFormComponent }
    ]
  },
  
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
