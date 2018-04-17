export class MessageSeverity {
  static readonly INFO = new MessageSeverity("INFO");
  static readonly WARN = new MessageSeverity("WARN");
  static readonly ERROR = new MessageSeverity("ERROR");
  static readonly SYSTEM = new MessageSeverity("SYSTEM");

  private static readonly VALUES: MessageSeverity[] = [MessageSeverity.INFO, MessageSeverity.WARN, MessageSeverity.ERROR, MessageSeverity.SYSTEM];

  static retrieveValueFromLabel(label:string) {
    if (!label) {
      return null;
    }

    const matches: MessageSeverity[] = this.VALUES.filter((value) => {
      return value.toString() === label;
    });

    if (matches.length === 0) {
      return null;
    }

    return matches[0];
  }

  private label: string

  constructor(private newLabel: string) {
    this.label = newLabel;
  }

  toString() {
    return this.label;
  }
}
