import { TestBed, async } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { MessageManagerComponent } from '../modules/MessageManager/message-manager.component';
import { MessageManager } from '../modules/MessageManager/message-manager.service';
import { LoginComponent } from '../modules/Login/login.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ClockComponent } from '../modules/Clock/clock.component';
import { LoginService } from '../modules/Login/login.service';
import { ClockService } from '../modules/Clock/clock.service';

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        MessageManagerComponent,
        LoginComponent,
        ClockComponent
      ],
      imports: [RouterTestingModule],
      providers: [
        {provide: MessageManager, useValue: new MessageManager()},
        {provide: LoginService, useValue: new LoginService()},
        {provide: ClockService, useValue: new ClockService()}
      ]
    }).compileComponents();
  }));
  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
});
