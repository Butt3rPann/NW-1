async function getItems(url, sortField, sortDirection, brands, filterStorages, filterPriceLower, filterPriceUpper, keyword, page, size) {
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

    if (keyword) {
      params.append('keyword', keyword)
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

async function uploadFormData(url, formData) {
  try {
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

async function updateFormData(url, id, formData) {
  try {
    const res = await fetch(`${url}/${id}`, {
      method: 'PUT',
      body: formData
    })
    const editedItem = await res.json()
    return editedItem
  } catch (error) {
    throw new Error('can not edit your item')
  }
}



export { getItems, getItemById, addItem , editItem , deleteItemById, uploadFormData, updateFormData}