import { TestBed, async } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { MessageManagerComponent } from '../modules/MessageManager/message-manager.component';
import { MessageManager } from '../modules/MessageManager/message-manager.service';
describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        MessageManagerComponent
      ],
      providers: [
        {provide: MessageManager, useValue: new MessageManager()}
      ]
    }).compileComponents();
  }));
  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
});
