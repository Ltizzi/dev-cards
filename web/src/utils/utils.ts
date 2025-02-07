function generateRandomId() {
  return Math.floor(Math.random() * 1000000 - 1) + 1; //removed Number.MAX_SAFE_INTEGER
}

export const utils = {
  generateRandomId,
};
