import { jwtDecode } from "jwt-decode";
import { acceptHMRUpdate, defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore('user', () => {
    const nickName = ref('')
    const access_token = ref('')
    const userId = ref(null)
    const userType = ref(null)
    
    function storeAccessToken(token) {
        localStorage.setItem('access_token', token)
        nickName.value = jwtDecode(token).nickname
        userId.value = jwtDecode(token).id
        userType.value = jwtDecode(token).role
    }

    function getAccessToken() {
        return localStorage.getItem('access_token')
    }

    function getNickname() {
        access_token.value = getAccessToken()
        nickName.value = access_token.value ? jwtDecode(access_token.value).nickname : ''
        return nickName.value
    }

    function getUserId() {
        access_token.value = getAccessToken()
        userId.value = access_token.value ? jwtDecode(access_token.value).id : null
        return userId.value
    }

    function getUserType() {
        access_token.value = getAccessToken()
        userType.value = access_token.value ? jwtDecode(access_token.value).role : null
        return userType.value
    }

    function removeAccessToken() {
        localStorage.removeItem('access_token')
        access_token.value = ''
        nickName.value = ''
        userId.value = null
        userType.value = null
    }

    const cart = ref([])

    function getCart() {
        return JSON.parse(localStorage.getItem('cart')) || []
    }

    function storeCart() {
        localStorage.setItem('cart', JSON.stringify(cart.value))
    }

    function addToCart(sellerId, sellerName, item) {
        cart.value = getCart()
        const indexOfSeller = cart.value.findIndex(obj => obj.sellerId === sellerId)
        if (indexOfSeller !== -1) {
            const indexOfItem = cart.value[indexOfSeller].saleItems.findIndex(obj => obj.id === item.id)
            if (indexOfItem !== -1) {
                cart.value[indexOfSeller].saleItems[indexOfItem].quantity += item.quantity
            } else {
                cart.value[indexOfSeller].saleItems.push(item)
            }
        } else {
            cart.value.push({ sellerId: sellerId, sellerName: sellerName, saleItems: [item] })
        }
        storeCart()
    }

    function removeFromCart(indexOfSeller, indexOfItem) {
        cart.value = getCart()
        if (cart.value[indexOfSeller].saleItems.length === 1) {
            cart.value.splice(indexOfSeller, 1)
        } else { 
            cart.value[indexOfSeller].saleItems.splice(indexOfItem, 1)
        }
        storeCart()
    }

    return { nickName, storeAccessToken, getAccessToken, removeAccessToken, getNickname, 
             userId, getUserId, 
             userType, getUserType,
             cart, getCart, addToCart, removeFromCart, storeCart }
})

if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useUserStore, import.meta.hot))
}