// utils/dms.js
export function dmsToDecimal(dms) {
    const regex = /(\d+)°(\d+)'(\d+)"?([NSEW])/;
    const match = dms.match(regex);
    if (!match) return parseFloat(dms);
  
    const [, degrees, minutes, seconds, dir] = match;
    let decimal = parseInt(degrees) + parseInt(minutes) / 60 + parseInt(seconds) / 3600;
    if (dir === 'S' || dir === 'W') decimal *= -1;
    return parseFloat(decimal.toFixed(6));
  }
  