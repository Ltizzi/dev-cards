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

export const dateUtils = {
  generateDateTemplate,
};
