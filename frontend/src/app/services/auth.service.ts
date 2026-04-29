import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap, map } from 'rxjs';
import { environment } from '../../environments/environment';

export interface User {
  id: number;
  username: string;
  realName: string;
  userType: 'user' | 'admin';
}

export interface LoginResponse {
  token: string;
  username: string;
  userType: string;
  userId: number;
  realName: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  realName: string;
  idCard: string;
  phone: string;
  email?: string;
  gender: string;
  birthDate: string;
  emergencyContact?: string;
  emergencyPhone?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User | null>;

  constructor(private http: HttpClient) {
    const storedUser = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<User | null>(
      storedUser ? JSON.parse(storedUser) : null
    );
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUserId(): number | null {
    const user = this.currentUserValue;
    return user ? user.id : null;
  }

  isLoggedIn(): boolean {
    return !!this.currentUserValue;
  }

  isAdmin(): boolean {
    const user = this.currentUserValue;
    return user?.userType === 'admin';
  }

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<{ success: boolean; message: string; data: LoginResponse }>(
      `${this.apiUrl}/login`,
      { username, password }
    ).pipe(
      map(response => response.data),
      tap((response: LoginResponse) => {
        const user: User = {
          id: response.userId,
          username: response.username,
          realName: response.realName,
          userType: response.userType as 'user' | 'admin'
        };
        localStorage.setItem('token', response.token);
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
      })
    );
  }

  register(request: RegisterRequest): Observable<any> {
    return this.http.post<{ success: boolean; message: string; data: any }>(
      `${this.apiUrl}/register`,
      request
    ).pipe(
      map(response => response.data)
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}
