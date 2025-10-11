import { jwtDecode } from "jwt-decode";
import { acceptHMRUpdate, defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore('user', () => {
    const nickName = ref('')
    const access_token = ref('')
    const userId = ref(null)
    const userType = ref(null)

    function isLoggedIn() {
        return !!localStorage.getItem('access_token')
    }
    
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

    function setCart(newCart) {
        cart.value = newCart;
    }

    function addToCart(item) {
        const {seller, ...cartItem} = item
        const indexOfSeller = cart.value.findIndex(obj => obj.seller.id === seller.id)
        if (indexOfSeller !== -1) {
            const indexOfItem = cart.value[indexOfSeller].cartItems.findIndex(obj => obj.saleItemId === cartItem.saleItemId)
            if (indexOfItem !== -1) {
                cart.value[indexOfSeller].cartItems[indexOfItem].quantity = cartItem.quantity
            } else {
                cart.value[indexOfSeller].cartItems.push(cartItem)
            }
        } else {
            cart.value.push({ seller, cartItems: [cartItem] })
        }
    }

    function removeFromCart(indexOfSeller, indexOfItem) {
        if (cart.value[indexOfSeller].cartItems.length === 1) {
            cart.value.splice(indexOfSeller, 1)
        } else { 
            cart.value[indexOfSeller].cartItems.splice(indexOfItem, 1)
        }
    }

    function updateCartItemQty(indexOfSeller, indexOfItem, newQty) {
        cart.value[indexOfSeller].cartItems[indexOfItem].quantity = newQty
    }

    function getCartItemCount() {
        return cart.value.reduce((total, seller) => total + seller.cartItems.reduce((sum, item) => sum + item.quantity, 0), 0)
    }

    const sellerOrdersCount = ref(0)

    function setSellerOrdersCount(count) {
        sellerOrdersCount.value = count
    }

    function getSellerOrdersCount() {
        return sellerOrdersCount.value
    }

    return { nickName, storeAccessToken, getAccessToken, removeAccessToken, getNickname, isLoggedIn,
             userId, getUserId, 
             userType, getUserType,
             cart, setCart, addToCart, removeFromCart, getCartItemCount, updateCartItemQty,
             sellerOrdersCount, setSellerOrdersCount, getSellerOrdersCount}
})

if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useUserStore, import.meta.hot))
}