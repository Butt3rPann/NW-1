import { jwtDecode } from "jwt-decode";
import { acceptHMRUpdate, defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore('user', () => {
    const accessToken = ref('')
    const nickName = ref('')
    const decoded = ref('')
     
    function storeToken(token) {
        accessToken.value = token
        decoded.value = jwtDecode(token)
        nickName.value = decoded.value.nickname
    }

    return {accessToken, storeToken, nickName, decoded}
})

if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useUserStore, import.meta.hot))
}