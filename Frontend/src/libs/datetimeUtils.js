function formatLocalTime(utcString) {
    const date = new Date(utcString).toLocaleString()
    return date
}

export { formatLocalTime }