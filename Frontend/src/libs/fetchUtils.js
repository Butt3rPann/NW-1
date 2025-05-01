async function getSaleItems(url) {
    try {
        const data = await fetch(url)
        const items = await data.json()
        return items
   } catch (error) {
        throw new Error('no sale item')
   }
}

export { getSaleItems }