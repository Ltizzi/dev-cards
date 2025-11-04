export {};

declare global {
  interface Window {
    appControl: {
      setProjectTitle(title: string): void;
    };
  }
}
