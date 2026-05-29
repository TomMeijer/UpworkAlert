export enum ChatRole {
  USER = 'USER',
  ASSISTANT = 'ASSISTANT'
}

export interface ChatMessage {
  id: number;
  role: ChatRole;
  time: string;
  content: string;
}
