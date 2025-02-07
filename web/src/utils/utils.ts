function generateRandomId() {

  return Math.floor(Math.random() * 1000000 - 1) + 1; //removed Number Number.MIN_SAFE_INTEGER

}

export const utils = {
  generateRandomId,
};
