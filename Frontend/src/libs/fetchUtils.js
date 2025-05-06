async function getSaleItems(url) {
  try {
    const data = await fetch(url)
    const items = await data.json()
    return items
  } catch (error) {
    throw new Error('no sale item')
  }
}

async function getSaleItemById(url, id) {
  try {
    const data = await fetch(`${url}/${id}`)
    const item = await data.json()
    return item
  } catch (error) {
    if (data.status === 404) return undefined
    throw new Error('The requested sale item does not exist')
  }
}

async function addSaleItem(url, newSaleItem) {
  try {
    const res = await fetch(url, {
      method: 'POST',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify({
        ...newSaleItem
      })
    })
    const addedItem = await res.json()
    return addedItem
  } catch (error) {
    throw new Error('can not add your sale item')
  }
}

export { getSaleItems, getSaleItemById, addSaleItem }