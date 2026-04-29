import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Registration {
  id: number;
  userId: number;
  eventId: number;
  groupId: number;
  status: 'pending' | 'approved' | 'rejected' | 'cancelled';
  shirtSize: 'XS' | 'S' | 'M' | 'L' | 'XL' | 'XXL' | 'XXXL';
  emergencyContact?: string;
  emergencyPhone?: string;
  medicalHistory?: string;
  remark?: string;
  reviewRemark?: string;
  reviewedBy?: number;
  reviewedAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface RegistrationCreate {
  eventId: number;
  groupId: number;
  shirtSize: string;
  emergencyContact?: string;
  emergencyPhone?: string;
  medicalHistory?: string;
  remark?: string;
}

export interface GroupStatistics {
  groupId: number;
  groupName: string;
  distance: string;
  maxParticipants: number;
  registeredCount: number;
  remainingCount: number;
}

export interface RegistrationStatistics {
  groupStatistics: GroupStatistics[];
  totalRegistered: number;
  totalCapacity: number;
  totalRemaining: number;
}

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // 用户方法
  getMyRegistrations(): Observable<Registration[]> {
    return this.http.get<{ success: boolean; message: string; data: Registration[] }>(
      `${this.apiUrl}/registrations/my`
    ).pipe(map(response => response.data));
  }

  createRegistration(registration: RegistrationCreate): Observable<Registration> {
    return this.http.post<{ success: boolean; message: string; data: Registration }>(
      `${this.apiUrl}/registrations`,
      registration
    ).pipe(map(response => response.data));
  }

  cancelRegistration(id: number): Observable<Registration> {
    return this.http.put<{ success: boolean; message: string; data: Registration }>(
      `${this.apiUrl}/registrations/${id}/cancel`,
      {}
    ).pipe(map(response => response.data));
  }

  // 管理员方法
  getEventRegistrations(eventId: number): Observable<Registration[]> {
    return this.http.get<{ success: boolean; message: string; data: Registration[] }>(
      `${this.apiUrl}/registrations/event/${eventId}`
    ).pipe(map(response => response.data));
  }

  getRegistrationById(id: number): Observable<Registration> {
    return this.http.get<{ success: boolean; message: string; data: Registration }>(
      `${this.apiUrl}/registrations/${id}`
    ).pipe(map(response => response.data));
  }

  reviewRegistration(id: number, status: string, reviewRemark?: string): Observable<Registration> {
    return this.http.put<{ success: boolean; message: string; data: Registration }>(
      `${this.apiUrl}/registrations/${id}/review`,
      { status, reviewRemark }
    ).pipe(map(response => response.data));
  }

  batchReview(registrationIds: number[], status: string, reviewRemark?: string): Observable<Registration[]> {
    return this.http.post<{ success: boolean; message: string; data: Registration[] }>(
      `${this.apiUrl}/registrations/batch-review`,
      { registrationIds, status, reviewRemark }
    ).pipe(map(response => response.data));
  }

  getRegistrationStatistics(eventId: number): Observable<RegistrationStatistics> {
    return this.http.get<{ success: boolean; message: string; data: RegistrationStatistics }>(
      `${this.apiUrl}/registrations/statistics/event/${eventId}`
    ).pipe(map(response => response.data));
  }

  exportEventRegistrations(eventId: number): Observable<Blob> {
    return this.http.get(
      `${this.apiUrl}/export/registrations/event/${eventId}`,
      { responseType: 'blob' }
    );
  }
}
