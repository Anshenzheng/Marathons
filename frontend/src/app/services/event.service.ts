import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Event {
  id: number;
  name: string;
  description: string;
  eventDate: string;
  eventTime: string;
  location: string;
  registrationStartDate: string;
  registrationEndDate: string;
  status: 'draft' | 'published' | 'offline' | 'ended';
  registrationStatus?: 'not_started' | 'active' | 'ended' | 'offline' | 'draft';
  createdBy: number;
  createdAt: string;
  updatedAt: string;
}

export interface EventGroup {
  id: number;
  eventId: number;
  name: string;
  distance: string;
  maxParticipants: number;
  registrationFee: number;
  ageMin?: number;
  ageMax?: number;
  genderLimit: 'all' | 'male' | 'female';
}

export interface Announcement {
  id: number;
  title: string;
  content: string;
  eventId?: number;
  type: 'general' | 'event';
  isTop: number;
  status: 'draft' | 'published';
  createdBy: number;
  createdAt: string;
  updatedAt: string;
}

export interface Regulation {
  id: number;
  eventId: number;
  title: string;
  content: string;
  version: number;
  status: 'draft' | 'published';
  createdBy: number;
  createdAt: string;
  updatedAt: string;
}

export interface PickupNote {
  id: number;
  eventId: number;
  title: string;
  content: string;
  pickupDate?: string;
  pickupLocation?: string;
  status: 'draft' | 'published';
  createdBy: number;
  createdAt: string;
  updatedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // 公开方法
  getPublishedEvents(): Observable<Event[]> {
    return this.http.get<{ success: boolean; message: string; data: Event[] }>(
      `${this.apiUrl}/events/public/list`
    ).pipe(map(response => response.data));
  }

  getActiveEvents(): Observable<Event[]> {
    return this.http.get<{ success: boolean; message: string; data: Event[] }>(
      `${this.apiUrl}/events/public/active`
    ).pipe(map(response => response.data));
  }

  getUpcomingEvents(): Observable<Event[]> {
    return this.http.get<{ success: boolean; message: string; data: Event[] }>(
      `${this.apiUrl}/events/public/upcoming`
    ).pipe(map(response => response.data));
  }

  getEventById(id: number): Observable<Event> {
    return this.http.get<{ success: boolean; message: string; data: Event }>(
      `${this.apiUrl}/events/public/${id}`
    ).pipe(map(response => response.data));
  }

  getEventGroups(eventId: number): Observable<EventGroup[]> {
    return this.http.get<{ success: boolean; message: string; data: EventGroup[] }>(
      `${this.apiUrl}/events/public/${eventId}/groups`
    ).pipe(map(response => response.data));
  }

  getPublishedAnnouncements(): Observable<Announcement[]> {
    return this.http.get<{ success: boolean; message: string; data: Announcement[] }>(
      `${this.apiUrl}/announcements/public/list`
    ).pipe(map(response => response.data));
  }

  getAnnouncementById(id: number): Observable<Announcement> {
    return this.http.get<{ success: boolean; message: string; data: Announcement }>(
      `${this.apiUrl}/announcements/public/${id}`
    ).pipe(map(response => response.data));
  }

  getEventAnnouncements(eventId: number): Observable<Announcement[]> {
    return this.http.get<{ success: boolean; message: string; data: Announcement[] }>(
      `${this.apiUrl}/announcements/public/event/${eventId}`
    ).pipe(map(response => response.data));
  }

  getPublishedRegulation(eventId: number): Observable<Regulation> {
    return this.http.get<{ success: boolean; message: string; data: Regulation }>(
      `${this.apiUrl}/regulations/public/event/${eventId}`
    ).pipe(map(response => response.data));
  }

  getPublishedPickupNote(eventId: number): Observable<PickupNote> {
    return this.http.get<{ success: boolean; message: string; data: PickupNote }>(
      `${this.apiUrl}/pickup-notes/public/event/${eventId}`
    ).pipe(map(response => response.data));
  }

  // 管理员方法
  createEvent(event: Partial<Event>): Observable<Event> {
    return this.http.post<{ success: boolean; message: string; data: Event }>(
      `${this.apiUrl}/events`,
      event
    ).pipe(map(response => response.data));
  }

  updateEvent(id: number, event: Partial<Event>): Observable<Event> {
    return this.http.put<{ success: boolean; message: string; data: Event }>(
      `${this.apiUrl}/events/${id}`,
      event
    ).pipe(map(response => response.data));
  }

  publishEvent(id: number): Observable<Event> {
    return this.http.put<{ success: boolean; message: string; data: Event }>(
      `${this.apiUrl}/events/${id}/publish`,
      {}
    ).pipe(map(response => response.data));
  }

  offlineEvent(id: number): Observable<Event> {
    return this.http.put<{ success: boolean; message: string; data: Event }>(
      `${this.apiUrl}/events/${id}/offline`,
      {}
    ).pipe(map(response => response.data));
  }

  deleteEvent(id: number): Observable<void> {
    return this.http.delete<{ success: boolean; message: string; data: void }>(
      `${this.apiUrl}/events/${id}`
    ).pipe(map(response => response.data));
  }

  createEventGroup(eventId: number, group: Partial<EventGroup>): Observable<EventGroup> {
    return this.http.post<{ success: boolean; message: string; data: EventGroup }>(
      `${this.apiUrl}/events/${eventId}/groups`,
      group
    ).pipe(map(response => response.data));
  }

  updateEventGroup(groupId: number, group: Partial<EventGroup>): Observable<EventGroup> {
    return this.http.put<{ success: boolean; message: string; data: EventGroup }>(
      `${this.apiUrl}/events/groups/${groupId}`,
      group
    ).pipe(map(response => response.data));
  }

  deleteEventGroup(groupId: number): Observable<void> {
    return this.http.delete<{ success: boolean; message: string; data: void }>(
      `${this.apiUrl}/events/groups/${groupId}`
    ).pipe(map(response => response.data));
  }

  createAnnouncement(announcement: Partial<Announcement>): Observable<Announcement> {
    return this.http.post<{ success: boolean; message: string; data: Announcement }>(
      `${this.apiUrl}/announcements`,
      announcement
    ).pipe(map(response => response.data));
  }

  updateAnnouncement(id: number, announcement: Partial<Announcement>): Observable<Announcement> {
    return this.http.put<{ success: boolean; message: string; data: Announcement }>(
      `${this.apiUrl}/announcements/${id}`,
      announcement
    ).pipe(map(response => response.data));
  }

  publishAnnouncement(id: number): Observable<Announcement> {
    return this.http.put<{ success: boolean; message: string; data: Announcement }>(
      `${this.apiUrl}/announcements/${id}/publish`,
      {}
    ).pipe(map(response => response.data));
  }

  deleteAnnouncement(id: number): Observable<void> {
    return this.http.delete<{ success: boolean; message: string; data: void }>(
      `${this.apiUrl}/announcements/${id}`
    ).pipe(map(response => response.data));
  }

  createRegulation(regulation: Partial<Regulation>): Observable<Regulation> {
    return this.http.post<{ success: boolean; message: string; data: Regulation }>(
      `${this.apiUrl}/regulations`,
      regulation
    ).pipe(map(response => response.data));
  }

  updateRegulation(id: number, regulation: Partial<Regulation>): Observable<Regulation> {
    return this.http.put<{ success: boolean; message: string; data: Regulation }>(
      `${this.apiUrl}/regulations/${id}`,
      regulation
    ).pipe(map(response => response.data));
  }

  publishRegulation(id: number): Observable<Regulation> {
    return this.http.put<{ success: boolean; message: string; data: Regulation }>(
      `${this.apiUrl}/regulations/${id}/publish`,
      {}
    ).pipe(map(response => response.data));
  }

  deleteRegulation(id: number): Observable<void> {
    return this.http.delete<{ success: boolean; message: string; data: void }>(
      `${this.apiUrl}/regulations/${id}`
    ).pipe(map(response => response.data));
  }

  createPickupNote(note: Partial<PickupNote>): Observable<PickupNote> {
    return this.http.post<{ success: boolean; message: string; data: PickupNote }>(
      `${this.apiUrl}/pickup-notes`,
      note
    ).pipe(map(response => response.data));
  }

  updatePickupNote(id: number, note: Partial<PickupNote>): Observable<PickupNote> {
    return this.http.put<{ success: boolean; message: string; data: PickupNote }>(
      `${this.apiUrl}/pickup-notes/${id}`,
      note
    ).pipe(map(response => response.data));
  }

  publishPickupNote(id: number): Observable<PickupNote> {
    return this.http.put<{ success: boolean; message: string; data: PickupNote }>(
      `${this.apiUrl}/pickup-notes/${id}/publish`,
      {}
    ).pipe(map(response => response.data));
  }

  deletePickupNote(id: number): Observable<void> {
    return this.http.delete<{ success: boolean; message: string; data: void }>(
      `${this.apiUrl}/pickup-notes/${id}`
    ).pipe(map(response => response.data));
  }
}
