import { MessageSeverity } from './message-severity';

export class Message {
  private _text = '';
  private _severity: MessageSeverity = MessageSeverity.INFO;
  private _sequence = 0;
  private _entity: string = null;
  private _entityKey: number = null;
  private _entityField: string = null;
  private __sealed = false;

  get text() {
    return this._text;
  }

  get severity() {
    return this._severity;
  }

  get sequence() {
    return this._sequence;
  }

  /**
   * Return the optional entity associated with this message. Together with entityKey and entityField, this allows correlation of a message
   * to a template field.
   */
  get entity() {
    return this._entity;
  }

  /**
   * Return the optional entity key associated with this message. Together with entity and entityField, this allows correlation of a message
   * to a template field.
   */
  get entityKey() {
    return this._entityKey;
  }

  /**
   * Return the optional entity field associated with this message. Together with entity and entityKey, this allows correlation of a
   * message to a template field.
   */
  get entityField() {
    return this._entityField;
  }

  getSeverityAsString(): string {
    return this._severity.toString();
  }

  set text(newText: string) {
    if (this.__sealed) {
      throw new Error('set text called when sealed.');
    }

    this._text = newText;
  }

  set severity(newSeverity: MessageSeverity | string) {
    if (this.__sealed) {
      throw new Error('set severity called when sealed.');
    }

    if (newSeverity instanceof MessageSeverity) {
      this._severity = newSeverity;

      return;
    }

    if (typeof newSeverity === 'string') {
      this._severity = MessageSeverity.retrieveValueFromLabel(newSeverity);

      return;
    }

    throw new Error('Unknown type passed to set severity: ' + newSeverity);
  }

  set sequence(newSequence: number) {
    if (this.__sealed) {
      throw new Error('set sequence called when sealed.');
    }

    this._sequence = newSequence;
  }

  set entity(newEntity: string) {
    if (this.__sealed) {
      throw new Error('set entity called when sealed.');
    }

    this._entity = newEntity;
  }

  set entityKey(newEntityKey: number) {
    if (this.__sealed) {
      throw new Error('set entity key called when sealed.');
    }

    this._entityKey = newEntityKey;
  }

  set entityField(newEntityField: string) {
    if (this.__sealed) {
      throw new Error('set entity field called when sealed.');
    }

    this._entityField = newEntityField;
  }

  seal() {
    this.__sealed = true;
  }
}
