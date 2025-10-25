import router from "@/router"
import { useUserStore } from "@/stores/UserStore"

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
      params.append('searchKeyWord', keyword)
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

async function getItemById(url, id, access_token) {
  try {
    const headers = new Headers();
    if (access_token) {
      const authHeader = `Bearer ${access_token}`
      headers.append("Authorization", authHeader)
    }
    const res = await fetch(`${url}/${id}`, { headers })

    if (res.status === 401) {
      const newToken = await refreshToken()
      if (!newToken) {
        throw new Error('Failed to refresh token')
      }
      const retryHeaders = new Headers(headers)
      retryHeaders.set('Authorization', `Bearer ${newToken}`)
      const newRes = await fetch(`${url}/${id}`, { headers: retryHeaders })
      if (newRes.status === 401) {
        throw new Error('Unauthorized even after refresh');
      }
      return await newRes.json()
    }

    const item = await res.json()
    return item
  } catch (error) {
    throw new Error('The requested item does not exist')
  }
}

async function getItemByIdWithToken(url, access_token, page, size, tab) {
  try {
    const headers = new Headers();
    if (access_token) {
      const authHeader = `Bearer ${access_token}`
      headers.append("Authorization", authHeader)
    }
    
    const params = new URLSearchParams()
    params.append('page', page)
    if (size) {
      params.append('size', size)
    }
    if (tab) {
      params.append('tab', tab)
    }
    const fullUrl = params.toString() ? `${url}?${params.toString()}` : url
    const res = await fetch(fullUrl, {
      headers
    })

    if (res.status === 401 && access_token) {
      const newToken = await refreshToken()
      if (!newToken) {
        throw new Error('Failed to refresh token')
      }
      const retryHeaders = new Headers(headers)
      retryHeaders.set('Authorization', `Bearer ${newToken}`)
      const newRes = await fetch(fullUrl, { headers: retryHeaders })
      if (newRes.status === 401) {
        throw new Error('Unauthorized even after refresh');
      }
      return await newRes.json()
    }

    const item = await res.json()
    return item
  } catch (error) {
    throw new Error('The requested item does not exist')
  }
}

async function postData(url, data, access_token) {
  try {
    const headers = new Headers();
    headers.append("content-type", "application/json");
    if (access_token) {
      const authHeader = `Bearer ${access_token}`
      headers.append("Authorization", authHeader)
    }
    const res = await fetch(url, {
      method: 'POST',
      headers,
      credentials: 'include',
      body: JSON.stringify(data)
    })
    if (res.status === 204) {
      return { status: res.status }
    }
    if (res.status === 401 && access_token) {
      const newToken = await refreshToken()
      if (!newToken) {
        throw new Error('Failed to refresh token');
      }
      const retryHeaders = new Headers(headers)
      retryHeaders.set('Authorization', `Bearer ${newToken}`)
      const newRes = await fetch(url, {
        method: 'POST',
        headers: retryHeaders,
        body: JSON.stringify(data)
      })
      if (newRes.status === 401) {
        throw new Error('Unauthorized even after refresh');
      }
      return await newRes.json()
    }

    const item = await res.json()
    return item
  } catch (error) {
      throw new Error('Failed to POST data')
  }
}

async function editItem(url, id, editItem, access_token) {
  try {
    const headers = new Headers();
    headers.append("content-type", "application/json");
    if (access_token) {
      const authHeader = `Bearer ${access_token}`
      headers.append("Authorization", authHeader)
    }

    const res = await fetch(`${url}/${id}`, {
      method: 'PUT',
      headers,
      body: JSON.stringify(editItem)
    })    

    if (res.status === 401 && access_token) {
      const newToken = await refreshToken()
      if (!newToken) {
        throw new Error('Failed to refresh token')
      }
      const retryHeaders = new Headers(headers)
      retryHeaders.set('Authorization', `Bearer ${newToken}`)
      const newRes = await fetch(`${url}/${id}`, {
        method: 'PUT',
        headers : retryHeaders,
        body: JSON.stringify(editItem)
      })   
      if (newRes.status === 401) {
        throw new Error('Unauthorized even after refresh');
      }
      return await newRes.json()
    }

    const editedItem = await res.json()
    return editedItem
  } catch (error) {
    throw new Error("can not edit your item");
  }
}

async function patchItem(url, id, partialItem, accessToken) {
  try {
    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    if (accessToken) {
      headers.append("Authorization", `Bearer ${accessToken}`);
    }

    const res = await fetch(`${url}/${id}`, {
      method: 'PATCH',
      headers,
      body: JSON.stringify(partialItem)
    })

    if (res.status === 401 && accessToken) {
      const newToken = await refreshToken()
      if (!newToken) {
        throw new Error('Failed to refresh token')
      }
      const retryHeaders = new Headers(headers)
      retryHeaders.set('Authorization', `Bearer ${newToken}`)
      const newRes = await fetch(`${url}/${id}`, {
        method: 'PATCH',
        headers : retryHeaders,
        body: JSON.stringify(partialItem)
      })   
      if (newRes.status === 401) {
        throw new Error('Unauthorized even after refresh');
      }
      return await newRes.json()
    }

    const updatedItem = await res.json();
    return updatedItem;
  } catch (error) {
    throw new Error("Cannot patch this item");
  }
}


async function deleteItemById(url, id, access_token) {
  try {
    const headers = new Headers();
    if (access_token) {
      const authHeader = `Bearer ${access_token}`
      headers.append("Authorization", authHeader)
    }
    const res = await fetch(`${url}/${id}`, {
      method: 'DELETE',
      headers
    })

    if (res.status === 401 && access_token) {
      const newToken = await refreshToken()
      if (!newToken) {
        throw new Error('Failed to refresh token')
      }
      const retryHeaders = new Headers(headers)
      retryHeaders.set('Authorization', `Bearer ${newToken}`)
      const newRes = await fetch(`${url}/${id}`, {
        method: 'DELETE',
        headers : retryHeaders
      })   
      if (newRes.status === 401) {
        throw new Error('Unauthorized even after refresh');
      }
      return newRes.status
    }

    return res.status
  } catch (error) {
    throw new Error('can not delete your item')
  }
}

async function uploadFormData(url, formData, access_token) {
  try {
    const headers = new Headers();
    if (access_token) {
      const authHeader = `Bearer ${access_token}`
      headers.append("Authorization", authHeader)
    }
    const res = await fetch(url, {
      method: 'POST',
      body: formData,
      headers
    })

    if (res.status === 401 && access_token) {
      const newToken = await refreshToken()
      if (!newToken) {
        throw new Error('Failed to refresh token');
      }
      const retryHeaders = new Headers(headers)
      retryHeaders.set('Authorization', `Bearer ${newToken}`)
      const newRes = await fetch(url, {
        method: 'POST',
        headers: retryHeaders,
        body: formData
      })
      if (newRes.status === 401) {
        throw new Error('Unauthorized even after refresh');
      }
      return await newRes.json()
    }

    const addedItem = await res.json()
    return addedItem
  } catch (error) {
    throw new Error('can not add your item')
  }
}

async function updateFormData(url, id, formData, access_token) {
  try {
    const headers = new Headers();
    if (access_token) {
      const authHeader = `Bearer ${access_token}`
      headers.append("Authorization", authHeader)
    }
    const res = await fetch(`${url}/${id}`, {
      method: 'PUT',
      headers,
      body: formData
    })

    if (res.status === 401 && access_token) {
      const newToken = await refreshToken()
      if (!newToken) {
        throw new Error('Failed to refresh token');
      }
      const retryHeaders = new Headers(headers)
      retryHeaders.set('Authorization', `Bearer ${newToken}`)
      const newRes = await fetch(`${url}/${id}`, {
        method: 'PUT',
        headers: retryHeaders,
        body: formData
      })
      if (newRes.status === 401) {
        throw new Error('Unauthorized even after refresh');
      }
      return await newRes.json()
    }

    const editedItem = await res.json()
    return editedItem
  } catch (error) {
    throw new Error('can not edit your item')
  }
}

async function refreshToken() {
  try {
    const userStore = useUserStore()
    const res = await fetch(`${import.meta.env.VITE_APP_URL}/v2/auth/refresh`, { method : 'POST' , credentials: 'include'})
    if (!res.ok) {
      userStore.removeAccessToken()
      router.push({ name : 'SignIn' })
      throw new Error(`Failed to refresh token (status: ${res.status})`);
    }
    const data = await res.json()
    const newToken = data.access_token
    userStore.storeAccessToken(newToken)
    return newToken;
  } catch (error) {
    throw new Error('Unable to refresh token.');
  }
}


export { getItems, getItemById, postData, editItem, patchItem, deleteItemById, uploadFormData, updateFormData, getItemByIdWithToken}