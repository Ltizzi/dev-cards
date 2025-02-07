function generateRandomId() {

  return Math.floor(Math.random() * 100000) + 1; //removed Number Number.MIN_SAFE_INTEGER

}

export const utils = {
  generateRandomId,
};
