import { jwtDecode } from "jwt-decode";
import { acceptHMRUpdate, defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore('user', () => {
    const nickName = ref('')
    const access_token = ref('')
    const userId = ref(null)
    const userType = ref(null)
    
    function storeAccessToken(token) {
        
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
        nickName.value = access_token.value ? jwtDecode(access_token.value).id : null
        return nickName.value
    }

    function getUserType() {
        access_token.value = getAccessToken()
        userType.value = access_token.value ? jwtDecode(access_token.value).role : null
        return userType.value
    }

    return { nickName, storeAccessToken, getAccessToken, getNickname, userId, getUserId, userType, getUserType }
})

if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useUserStore, import.meta.hot))
}