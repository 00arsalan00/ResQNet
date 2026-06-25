// Add a state for login type
const [loginType, setLoginType] = useState('password'); // 'password' or 'otp'

// In the UI, use a sleek segmented control:
<div className="flex bg-peppermint dark:bg-cello p-1 rounded-2xl border border-wedgewood/10 mb-8">
  <button 
    onClick={() => setLoginType('password')}
    className={`flex-1 py-2 rounded-xl text-[10px] font-black uppercase transition-all ${loginType === 'password' ? 'bg-white dark:bg-cello-dark shadow-sm text-amaranth' : 'text-wedgewood opacity-50'}`}
  >
    Standard Key
  </button>
  <button 
    onClick={() => setLoginType('otp')}
    className={`flex-1 py-2 rounded-xl text-[10px] font-black uppercase transition-all ${loginType === 'otp' ? 'bg-white dark:bg-cello-dark shadow-sm text-amaranth' : 'text-wedgewood opacity-50'}`}
  >
    Mobile Node
  </button>
</div>

// Then conditionally render the Password input OR the OTP "Send" input