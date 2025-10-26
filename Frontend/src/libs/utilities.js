export const previewBinaryFile = (binaryFileObject) => {
  return URL.createObjectURL(binaryFileObject)
}

export const maskNumber = (number) => {
  const numStr = String(number)
  const length = numStr.length
  let result = ''

  for (let i = 0; i < length; i++) {
    const isTargetDigit = i === length - 2 || i === length - 3 || i === length - 4;
    result += isTargetDigit ? numStr[i] : 'x'
  }

  return result
}