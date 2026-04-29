import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

// 服务
import { AuthService } from './services/auth.service';
import { EventService } from './services/event.service';
import { RegistrationService } from './services/registration.service';
import { AuthInterceptor } from './interceptors/auth.interceptor';

// 组件
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { NavbarComponent } from './components/navbar/navbar.component';
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

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    NavbarComponent,
    EventListComponent,
    EventDetailComponent,
    MyRegistrationsComponent,
    AdminDashboardComponent,
    AdminEventListComponent,
    AdminEventFormComponent,
    AdminRegistrationListComponent,
    AdminAnnouncementFormComponent,
    AdminRegulationFormComponent,
    AdminPickupNoteFormComponent,
    AnnouncementDetailComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    AppRoutingModule
  ],
  providers: [
    AuthService,
    EventService,
    RegistrationService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
