import { Message } from "../MessageManager/message";
import { MessageSeverity } from "../MessageManager/message-severity";
import { populateClassInstanceFromDto } from "./utilityMethods";

// Use the Message class as a target to validate the utility
describe("utilityMethods testing", () => {
  it("should update the Message correctly", () => {
    const message: Message = new Message();
    const dto: any = {
      text: "This is the text.",
      severity: "INFO",
      sequence: 5,
      other: "Other stuff not related to Message."
    };

    populateClassInstanceFromDto(message, dto);

    expect(message.text === dto.text);
    expect(message.severity === MessageSeverity.INFO);
    expect(message.sequence === dto.sequence);
    expect(message.entity === null);
    expect(message.entityKey === null);
    expect(message.entityField === null);
  });

  it("should seal the Message", () => {
    const message: Message = new Message();
    const dto: any = {
      text: "This is the text.",
      severity: "INFO",
      sequence: 5,
      other: "Other stuff not related to Message."
    };

    populateClassInstanceFromDto(message, dto);

    let errorThrown: boolean = false;
    try {
      message.text = "try to change text";
    } catch (err) {
      errorThrown = true;
    }

    expect(errorThrown);
  });
});
