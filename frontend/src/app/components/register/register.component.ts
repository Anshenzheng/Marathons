import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, RegisterRequest } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  loading = false;
  error = '';
  success = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    this.registerForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(20)]],
      confirmPassword: ['', Validators.required],
      realName: ['', Validators.required],
      idCard: ['', [Validators.required, Validators.pattern(/^\d{17}[\dXx]$/)]],
      phone: ['', [Validators.required, Validators.pattern(/^1[3-9]\d{9}$/)]],
      email: ['', Validators.email],
      gender: ['', Validators.required],
      birthDate: ['', Validators.required],
      emergencyContact: [''],
      emergencyPhone: ['', Validators.pattern(/^1[3-9]\d{9}$/)]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }
    return null;
  };

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
    }
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    const formValue = this.registerForm.value;
    const registerRequest: RegisterRequest = {
      username: formValue.username,
      password: formValue.password,
      realName: formValue.realName,
      idCard: formValue.idCard,
      phone: formValue.phone,
      email: formValue.email,
      gender: formValue.gender,
      birthDate: formValue.birthDate,
      emergencyContact: formValue.emergencyContact,
      emergencyPhone: formValue.emergencyPhone
    };

    this.authService.register(registerRequest).subscribe({
      next: () => {
        this.success = true;
        this.loading = false;
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err: any) => {
        this.error = err.error?.message || '注册失败，请稍后重试';
        this.loading = false;
      }
    });
  }

  private markAllAsTouched(): void {
    Object.keys(this.registerForm.controls).forEach(key => {
      this.registerForm.get(key)?.markAsTouched();
    });
  }
}
