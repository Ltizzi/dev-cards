import { v4 as uuidv4 } from "uuid";

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

function generateUUID() {
  return uuidv4();
}

function generateRealRandomId() {
  return Math.floor(Math.random() * Number.MAX_SAFE_INTEGER) + 1; //removed Number Number.MIN_SAFE_INTEGER
}

function textReduce(text: string, lenght: number) {
  return text.slice(0, lenght) + "(...)";
}

function fixDateFormat(date: Date) {
  let dateString = date.toString();
  if (!dateString) return null;

  // Si ya está en formato correcto (YYYY-MM-DD), no hacer nada
  if (/^\d{4}-\d{2}-\d{2}T/.test(dateString)) {
    return dateString;
  }

  // Si está en formato DD-MM-YYYY, convertir
  if (/^\d{2}-\d{2}-\d{4}T/.test(dateString)) {
    const [datePart, timePart] = dateString.split("T");
    const [day, month, year] = datePart.split("-");
    return `${year}-${month}-${day}T${timePart}`;
  }

  return dateString;
}

export const utils = {
  generateRandomId,
  generateRealRandomId,
  textReduce,
  fixDateFormat,
  generateUUID,
};
