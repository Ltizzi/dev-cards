function generateRandomId() {
  return Math.floor(Math.random() * Number.MAX_SAFE_INTEGER - 1) + 1;
}

export const utils = {
  generateRandomId,
};
