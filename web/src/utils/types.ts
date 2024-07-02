export enum HttpMethod {
  GET,
  POST,
  PATCH,
  DELETE,
}

export interface Endpoint {
  [key: string]: string;
}
