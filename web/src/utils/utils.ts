function generateRandomId() {
  const lastId = localStorage.getItem("lastId");
  if (!lastId) {
    localStorage.setItem("lastId", "1");
    return 1;
  } else {
    const id = +lastId + 1;
    localStorage.setItem("lastId", id.toString());
    return id;
  }

  //return Math.floor(Math.random() * 100000) + 1; //removed Number Number.MIN_SAFE_INTEGER
}

function generateRealRandomId() {
  return Math.floor(Math.random() * Number.MAX_SAFE_INTEGER) + 1; //removed Number Number.MIN_SAFE_INTEGER
}

function textReduce(text: string, lenght: number) {
  return text.slice(0, lenght) + "(...)";
}

export const utils = {
  generateRandomId,
  generateRealRandomId,
  textReduce,
};
