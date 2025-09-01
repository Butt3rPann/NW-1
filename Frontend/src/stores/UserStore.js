import { jwtDecode } from "jwt-decode";
import { acceptHMRUpdate, defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore('user', () => {
    const nickName = ref('')
    const access_token = ref('')
    
    function storeAccessToken(token) {
        localStorage.setItem('access_token', token)
        nickName.value = jwtDecode(token).nickname
    }

    function getAccessToken() {
        return localStorage.getItem('access_token')
    }

    function getNickname() {
        access_token.value = getAccessToken()
        nickName.value = access_token.value ? jwtDecode(access_token.value).nickname : ''
        return nickName.value
    }

    return { nickName, storeAccessToken, getAccessToken, getNickname }
})

if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useUserStore, import.meta.hot))
}