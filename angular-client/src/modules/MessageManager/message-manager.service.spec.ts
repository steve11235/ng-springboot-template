import { Message } from "./message";
import { MessageManager } from "./message-manager.service";
import { MessageSeverity } from "./message-severity";

describe("Test the MessageManager service", () => {
  let messages: Message[] = null;
  let messageManager: MessageManager = null;

  beforeEach(() => {
    messages = [];
    messageManager = new MessageManager();
    messageManager.setMessageListener((newMessages: Message[]) => {
      messages = newMessages;
    });
  });

  it("should add a message (no entity info)", () => {
    const text: string = "this is a message";
    const severity: MessageSeverity = MessageSeverity.WARN;

    messageManager.addMessage(text, severity);

    expect(messages.length).toEqual(1);
    const message: Message = messages[0];
    expect(message.text).toEqual(text);
    expect(message.severity).toEqual(severity);
    expect(message.entity).toEqual(null);
    expect(message.entityKey).toEqual(null);
    expect(message.entityField).toEqual(null);
  });

  it("should add a message (with entity info)", () => {
    const text: string = "this is a message";
    const severity: MessageSeverity = MessageSeverity.WARN;
    const entity: string = "Foo";
    const entityKey: number = 123;
    const entityField: string = "Bar";

    messageManager.addMessage(text, severity, entity, entityKey, entityField);

    expect(messages.length).toEqual(1);
    const message: Message = messages[0];
    expect(message.text).toEqual(text);
    expect(message.severity).toEqual(severity);
    expect(message.entity).toEqual(entity);
    expect(message.entityKey).toEqual(entityKey);
    expect(message.entityField).toEqual(entityField);
  });

  it("should add multiple messages from a DTO array", () => {
    const dto: any[] = [
      {
        text: "this is a message",
        severity: "WARN"
      },
      {
        text: "this is an entity message",
        severity: "ERROR",
        entity: "Foo",
        entityKey: 123,
        entityField: "Bar"
      }
    ];

    messageManager.addMessages(dto);

    expect(messages.length).toEqual(2);
    const message1: Message = messages[0];
    expect(message1.text).toEqual("this is a message");
    expect(message1.severity).toEqual(MessageSeverity.WARN);
    expect(message1.entity).toEqual(null);
    expect(message1.entityKey).toEqual(null);
    expect(message1.entityField).toEqual(null);
    const message2: Message = messages[1];
    expect(message2.text).toEqual("this is an entity message");
    expect(message2.severity).toEqual(MessageSeverity.ERROR);
    expect(message2.entity).toEqual("Foo");
    expect(message2.entityKey).toEqual(123);
    expect(message2.entityField).toEqual("Bar");
  });

  it("should clear messages", () => {
    const text: string = "this is a message";
    const severity: MessageSeverity = MessageSeverity.WARN;

    messageManager.addMessage(text, severity);

    expect(messages.length).toEqual(1);

    messageManager.clearMessages();

    expect(messages.length).toEqual(0);
  });

  it("should retrieve only entity messages", () => {
    const dto: any[] = [
      {
        text: "this is a message",
        severity: "WARN"
      },
      {
        text: "this is an entity message",
        severity: "ERROR",
        entity: "Foo",
        entityKey: 123,
        entityField: "Bar"
      }
    ];

    messageManager.addMessages(dto);
    expect(messages.length).toEqual(2);

    const entityMessages: Message[] = messageManager.getEntityMessages();

    expect(entityMessages.length).toEqual(1);
    const message: Message = entityMessages[0];
    expect(message.text).toEqual("this is an entity message");
    expect(message.severity).toEqual(MessageSeverity.ERROR);
    expect(message.entity).toEqual("Foo");
    expect(message.entityKey).toEqual(123);
    expect(message.entityField).toEqual("Bar");
  });
});
