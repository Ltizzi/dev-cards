function generateDateTemplate(incDate: any) {
  let date = new Date(incDate);
  const year = date.getFullYear().toString();
  const month = (date.getMonth() + 1).toString();
  const day = date.getDate().toString();
  const hour = date.getHours().toString();
  const minutes = date.getMinutes().toString();

  return `${day} / ${month} / ${year} at ${
    hour.length < 2 ? "0" + hour : hour
  }:${minutes.length < 2 ? "0" + minutes : minutes}${
    +hour >= 12 && +hour < 24 ? " pm" : " am"
  }`;
}

function getActualDate() {
  const date = new Date();
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = String(date.getFullYear());
  return `${day}-${month}-${year}`;
}

const daysOfWeek = ["L", "M", "M", "J", "V", "S", "D"];
const daysOfWeekEN = ["M", "T", "W", "T", "F", "S", "S"];
const months = [
  "Enero",
  "Febrero",
  "Marzo",
  "Abril",
  "Mayo",
  "Junio",
  "Julio",
  "Agosto",
  "Septiembre",
  "Octubre",
  "Noviembre",
  "Diciembre",
];

export const dateUtils = {
  generateDateTemplate,
  getActualDate,
  daysOfWeek,
  daysOfWeekEN,
  months,
};
