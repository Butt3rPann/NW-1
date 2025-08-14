async function getItems(url, sortField, sortDirection, brands, filterPriceLower, filterPriceUpper,filterStorages, page, size) {
  try {
    const params = new URLSearchParams()

    if (sortField && sortDirection) {
      params.append('sortField', sortField)
      params.append('sortDirection', sortDirection)
    }

    if (Array.isArray(brands) && brands.length > 0) {
      const joinedBrands = brands.join(',')
      params.append('filterBrands', joinedBrands)
    }

    if (filterPriceLower || filterPriceLower === 0) {
      params.append('filterPriceLower', filterPriceLower)
    }

    if (filterPriceUpper || filterPriceUpper === 0) {
      params.append('filterPriceUpper', filterPriceUpper)
    }

    if (Array.isArray(filterStorages) && filterStorages.length > 0) {
      const joinedStorages = filterStorages.map(value => value === null ? -1 : value).join(',')
      params.append('filterStorages', joinedStorages)
    }
    
    if (page >= 0 && size) {
      params.append('page', page)
      params.append('size', size)
    }
    
    const fullUrl = params.toString() ? `${url}?${params.toString()}` : url
    const data = await fetch(fullUrl)
    const items = await data.json();
    return items
  } catch (error) {
    throw new Error('no item')
  }
}

async function getItemById(url, id) {
  try {
    const data = await fetch(`${url}/${id}`)
    const item = await data.json()
    return item
  } catch (error) {
    if (data.status === 404) return undefined
    throw new Error('The requested item does not exist')
  }
}

async function addItem(url, newItem) {
  try {
    const res = await fetch(url, {
      method: 'POST',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify({
        ...newItem
      })
    })
    const addedItem = await res.json()
    return addedItem
  } catch (error) {
    throw new Error('can not add your item')
  }
}

async function editItem(url, id, editItem) {
  try {
    const res = await fetch(`${url}/${id}`, {
      method: 'PUT',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify({
        ...editItem
      })
    })
    const editedItem = await res.json()
    return editedItem
  } catch (error) {
    throw new Error("can not edit your item");
  }
}

async function deleteItemById(url, id) {
  try {
    const res = await fetch(`${url}/${id}`, {
      method: 'DELETE'
    })
    return res.status
  } catch (error) {
    throw new Error('can not delete your item')
  }
}

async function uploadFormData(url, file, data) {
  try {
    const formData = new FormData()
    file.forEach(f => formData.append('files', f))
    if (data.model !== null) formData.append('model', data.model)
    if (data.brand.id !== null && data.brand.name !== null)  {
      formData.append('brand.id', data.brand.id)
      formData.append('brand.name', data.brand.name)
    }
    if (data.description !== null) formData.append('description', data.description)
    if (data.price !== null) formData.append('price', data.price)
    if (data.ramGb !== null) formData.append('ramGb', data.ramGb)
    if (data.screenSizeInch !== null) formData.append('screenSizeInch', data.screenSizeInch)
    if (data.storageGb !== null) formData.append('storageGb', data.storageGb)
    if (data.color !== null) formData.append('color', data.color)
    if (data.quantity !== null) formData.append('quantity', data.quantity)

    const res = await fetch(url, {
      method: 'POST',
      body: formData
    })
    const addedItem = await res.json()
    return addedItem
  } catch (error) {
    throw new Error('can not add your item')
  }
}

export { getItems, getItemById, addItem , editItem , deleteItemById, uploadFormData}