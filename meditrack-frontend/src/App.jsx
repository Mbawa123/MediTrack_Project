import { useState } from "react"
import axios from "axios"

const API = "http://localhost:7070"

// ═══════════════════════════════════════
// USERS DATABASE (hardcoded for demo)
// ═══════════════════════════════════════
const USERS = [
  { username: "admin",  password: "meditrack2026", role: "Admin",  name: "System Administrator" },
  { username: "doctor", password: "doctor123",     role: "Doctor", name: "Dr Chanda" },
  { username: "nurse",  password: "nurse123",      role: "Nurse",  name: "Nurse Phiri" },
]

// ═══════════════════════════════════════
// LOGIN SCREEN
// ═══════════════════════════════════════
function Login({ onLogin }) {
  const [form, setForm] = useState({})
  const [error, setError] = useState(null)
  const handle = e => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = e => {
    e.preventDefault()
    const user = USERS.find(u => u.username === form.username && u.password === form.password)
    if (user) { onLogin(user) }
    else { setError("Invalid credentials. Please try again.") }
  }
  return (
    <div style={{ minHeight:"100vh", display:"flex", alignItems:"center", justifyContent:"center" }}>
      <div style={{ width:"420px", background:"rgba(255,255,255,0.02)", border:"1px solid rgba(0,212,255,0.2)", borderRadius:"12px", padding:"48px", position:"relative" }}>
        <div style={{ position:"absolute", top:"-1px", left:"20px", right:"20px", height:"2px", background:"linear-gradient(90deg, transparent, #00d4ff, #7800ff, transparent)" }} />
        <div style={{ textAlign:"center", marginBottom:"40px" }}>
          <div style={{ fontSize:"48px", marginBottom:"12px" }}>🏨</div>
          <h1 style={{ fontSize:"28px", fontWeight:"800", letterSpacing:"4px", textTransform:"uppercase", background:"linear-gradient(90deg, #00d4ff, #7800ff)", WebkitBackgroundClip:"text", WebkitTextFillColor:"transparent", marginBottom:"8px" }}>MediTrack</h1>
          <p style={{ color:"#4a5a8a", fontSize:"12px", letterSpacing:"2px", textTransform:"uppercase" }}>Secure Access Portal</p>
        </div>
        <form onSubmit={submit}>
          <div className="form-group">
            <label>Username</label>
            <input name="username" placeholder="Enter username" onChange={handle} required autoComplete="off" />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input name="password" type="password" placeholder="Enter password" onChange={handle} required />
          </div>
          {error && <div style={{ background:"rgba(255,50,50,0.05)", color:"#ff6b6b", border:"1px solid rgba(255,50,50,0.3)", borderLeft:"3px solid #ff4444", padding:"12px 16px", borderRadius:"4px", fontSize:"13px", marginBottom:"16px" }}>❌ {error}</div>}
          <button type="submit" style={{ width:"100%", marginTop:"8px" }}>⚡ Login</button>
        </form>
        <div style={{ marginTop:"32px", padding:"16px", background:"rgba(0,212,255,0.03)", border:"1px solid rgba(0,212,255,0.1)", borderRadius:"4px" }}>
          <p style={{ color:"#4a5a8a", fontSize:"11px", letterSpacing:"1px", textTransform:"uppercase", marginBottom:"8px" }}>Demo Credentials</p>
          <p style={{ color:"#6b9fff", fontSize:"12px", marginBottom:"4px" }}>👑 admin / meditrack2026</p>
          <p style={{ color:"#6b9fff", fontSize:"12px", marginBottom:"4px" }}>🩺 doctor / doctor123</p>
          <p style={{ color:"#6b9fff", fontSize:"12px" }}>💉 nurse / nurse123</p>
        </div>
      </div>
    </div>
  )
}

// ═══════════════════════════════════════
// SHARED COMPONENTS
// ═══════════════════════════════════════
function Result({ result, isError }) {
  if (!result) return null
  return <pre className={`result ${isError ? "error" : "success"}`}>{isError ? "❌ ERROR\n" : "✅ SUCCESS\n"}{result}</pre>
}

// ═══════════════════════════════════════
// REGISTER PATIENT (Admin only)
// ═══════════════════════════════════════
function RegisterPatient() {
  const [type, setType] = useState("student")
  const [form, setForm] = useState({})
  const [result, setResult] = useState(null)
  const [isError, setIsError] = useState(false)
  const handle = e => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = async e => {
    e.preventDefault()
    try {
      const payload = { type, ...form, year: Number(form.year), yearsOfService: Number(form.yearsOfService) }
      const res = await axios.post(`${API}/patients/register`, payload)
      setResult(JSON.stringify(res.data, null, 2)); setIsError(false)
    } catch (err) {
      setResult(JSON.stringify(err.response?.data || err.message, null, 2)); setIsError(true)
    }
  }
  return (
    <div>
      <h2>Register Patient</h2>
      <p className="subtitle">Add a new student or staff patient to the system</p>
      <div className="form-group">
        <label>Patient Type</label>
        <select onChange={e => setType(e.target.value)}>
          <option value="student">Student Patient</option>
          <option value="staff">Staff Patient</option>
        </select>
      </div>
      <form onSubmit={submit}>
        <div className="form-row">
          <div className="form-group"><label>Patient ID</label><input name="id" placeholder="e.g. p1" onChange={handle} required /></div>
          <div className="form-group"><label>Full Name</label><input name="name" placeholder="e.g. Alice Banda" onChange={handle} required /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Email</label><input name="email" placeholder="e.g. alice@zut.ac.zm" onChange={handle} required /></div>
          <div className="form-group"><label>Phone</label><input name="phone" placeholder="e.g. 0977123456" onChange={handle} required /></div>
        </div>
        <div className="divider" />
        {type === "student" ? (
          <div className="form-row">
            <div className="form-group"><label>Student ID</label><input name="studentId" placeholder="e.g. S001" onChange={handle} required /></div>
            <div className="form-group"><label>Programme</label><input name="programme" placeholder="e.g. Computer Science" onChange={handle} required /></div>
            <div className="form-group"><label>Year</label><input name="year" type="number" placeholder="e.g. 2" onChange={handle} required /></div>
          </div>
        ) : (
          <div className="form-row">
            <div className="form-group"><label>Employee ID</label><input name="employeeId" placeholder="e.g. E001" onChange={handle} required /></div>
            <div className="form-group"><label>Department</label><input name="department" placeholder="e.g. Finance" onChange={handle} required /></div>
            <div className="form-group"><label>Years of Service</label><input name="yearsOfService" type="number" placeholder="e.g. 5" onChange={handle} required /></div>
          </div>
        )}
        <button type="submit">⚡ Register Patient</button>
      </form>
      <Result result={result} isError={isError} />
    </div>
  )
}

// ═══════════════════════════════════════
// REGISTER STAFF (Admin only)
// ═══════════════════════════════════════
function RegisterStaff() {
  const [type, setType] = useState("doctor")
  const [form, setForm] = useState({})
  const [result, setResult] = useState(null)
  const [isError, setIsError] = useState(false)
  const handle = e => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = async e => {
    e.preventDefault()
    try {
      const res = await axios.post(`${API}/staff/register`, { type, ...form })
      setResult(JSON.stringify(res.data, null, 2)); setIsError(false)
    } catch (err) {
      setResult(JSON.stringify(err.response?.data || err.message, null, 2)); setIsError(true)
    }
  }
  return (
    <div>
      <h2>Register Staff</h2>
      <p className="subtitle">Add a doctor or nurse to the clinic system</p>
      <div className="form-group">
        <label>Staff Type</label>
        <select onChange={e => setType(e.target.value)}>
          <option value="doctor">Doctor</option>
          <option value="nurse">Nurse</option>
        </select>
      </div>
      <form onSubmit={submit}>
        <div className="form-row">
          <div className="form-group"><label>ID</label><input name="id" placeholder="e.g. d1" onChange={handle} required /></div>
          <div className="form-group"><label>Full Name</label><input name="name" placeholder="e.g. Dr Chanda" onChange={handle} required /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Email</label><input name="email" placeholder="e.g. chanda@zut.ac.zm" onChange={handle} required /></div>
          <div className="form-group"><label>Phone</label><input name="phone" placeholder="e.g. 0955000001" onChange={handle} required /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Staff Reference ID</label><input name="staffId" placeholder="e.g. ST001" onChange={handle} required /></div>
          <div className="form-group"><label>Department</label><input name="department" placeholder="e.g. General" onChange={handle} required /></div>
        </div>
        <div className="divider" />
        {type === "doctor"
          ? <div className="form-group"><label>Licence Number</label><input name="licenceNumber" placeholder="e.g. LIC001" onChange={handle} required /></div>
          : <div className="form-group"><label>Nurse ID</label><input name="nurseId" placeholder="e.g. NR001" onChange={handle} required /></div>
        }
        <button type="submit">⚡ Register Staff</button>
      </form>
      <Result result={result} isError={isError} />
    </div>
  )
}

// ═══════════════════════════════════════
// BOOK APPOINTMENT (Admin + Doctor + Nurse)
// ═══════════════════════════════════════
function BookAppointment() {
  const [form, setForm] = useState({})
  const [result, setResult] = useState(null)
  const [isError, setIsError] = useState(false)
  const handle = e => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = async e => {
    e.preventDefault()
    try {
      const res = await axios.post(`${API}/appointments/book`, { patientId: form.patientId, staffId: form.staffId, dateTime: form.dateTime + ":00" })
      setResult(JSON.stringify(res.data, null, 2)); setIsError(false)
    } catch (err) {
      setResult(JSON.stringify(err.response?.data || err.message, null, 2)); setIsError(true)
    }
  }
  return (
    <div>
      <h2>Book Appointment</h2>
      <p className="subtitle">Schedule a patient with a doctor or nurse</p>
      <form onSubmit={submit}>
        <div className="form-row">
          <div className="form-group"><label>Patient ID</label><input name="patientId" placeholder="e.g. p1" onChange={handle} required /></div>
          <div className="form-group"><label>Staff ID</label><input name="staffId" placeholder="e.g. d1" onChange={handle} required /></div>
        </div>
        <div className="form-group"><label>Date and Time</label><input name="dateTime" type="datetime-local" onChange={handle} required /></div>
        <button type="submit">⚡ Book Appointment</button>
      </form>
      <Result result={result} isError={isError} />
    </div>
  )
}

// ═══════════════════════════════════════
// ADD DIAGNOSIS (Doctor only)
// ═══════════════════════════════════════
function AddDiagnosis() {
  const [form, setForm] = useState({})
  const [result, setResult] = useState(null)
  const [isError, setIsError] = useState(false)
  const handle = e => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = async e => {
    e.preventDefault()
    try {
      const res = await axios.post(`${API}/records/${form.patientId}/diagnosis`, { staffId: form.staffId, description: form.description })
      setResult(JSON.stringify(res.data, null, 2)); setIsError(false)
    } catch (err) {
      setResult(JSON.stringify(err.response?.data || err.message, null, 2)); setIsError(true)
    }
  }
  return (
    <div>
      <h2>Add Diagnosis</h2>
      <p className="subtitle">Only Doctors can add diagnoses — Nurses will receive 403 FORBIDDEN</p>
      <form onSubmit={submit}>
        <div className="form-row">
          <div className="form-group"><label>Patient ID</label><input name="patientId" placeholder="e.g. p1" onChange={handle} required /></div>
          <div className="form-group"><label>Staff ID</label><input name="staffId" placeholder="e.g. d1" onChange={handle} required /></div>
        </div>
        <div className="form-group">
          <label>Diagnosis Description</label>
          <textarea name="description" rows="4" placeholder="Describe the diagnosis..." onChange={handle} required style={{ resize:"vertical", width:"100%", padding:"12px", background:"rgba(0,212,255,0.03)", border:"1px solid rgba(0,212,255,0.2)", borderRadius:"4px", color:"#e0e6ff", fontFamily:"inherit" }} />
        </div>
        <button type="submit">⚡ Submit Diagnosis</button>
      </form>
      <Result result={result} isError={isError} />
    </div>
  )
}

// ═══════════════════════════════════════
// UPDATE VITALS (Nurse only)
// ═══════════════════════════════════════
function UpdateVitals() {
  const [form, setForm] = useState({})
  const [result, setResult] = useState(null)
  const [isError, setIsError] = useState(false)
  const handle = e => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = async e => {
    e.preventDefault()
    try {
      const res = await axios.put(`${API}/records/${form.patientId}/vitals`, {
        staffId: form.staffId,
        bloodPressure: form.bloodPressure,
        temperature: Number(form.temperature),
        weight: Number(form.weight)
      })
      setResult(JSON.stringify(res.data, null, 2)); setIsError(false)
    } catch (err) {
      setResult(JSON.stringify(err.response?.data || err.message, null, 2)); setIsError(true)
    }
  }
  return (
    <div>
      <h2>Update Vitals</h2>
      <p className="subtitle">Record patient vitals — blood pressure, temperature, weight</p>
      <form onSubmit={submit}>
        <div className="form-row">
          <div className="form-group"><label>Patient ID</label><input name="patientId" placeholder="e.g. p1" onChange={handle} required /></div>
          <div className="form-group"><label>Staff ID</label><input name="staffId" placeholder="e.g. n1" onChange={handle} required /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Blood Pressure</label><input name="bloodPressure" placeholder="e.g. 120/80" onChange={handle} required /></div>
          <div className="form-group"><label>Temperature (°C)</label><input name="temperature" type="number" step="0.1" placeholder="e.g. 36.6" onChange={handle} required /></div>
        </div>
        <div className="form-group"><label>Weight (kg)</label><input name="weight" type="number" step="0.1" placeholder="e.g. 65.0" onChange={handle} required /></div>
        <button type="submit">⚡ Update Vitals</button>
      </form>
      <Result result={result} isError={isError} />
    </div>
  )
}

// ═══════════════════════════════════════
// VIEW PATIENT (All roles)
// ═══════════════════════════════════════
function ViewPatient() {
  const [patientId, setPatientId] = useState("")
  const [patient, setPatient] = useState(null)
  const [summary, setSummary] = useState(null)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const search = async e => {
    e.preventDefault()
    setError(null); setPatient(null); setSummary(null); setLoading(true)
    try {
      const [pRes, sRes] = await Promise.all([
        axios.get(`${API}/patients/${patientId}`),
        axios.get(`${API}/patients/${patientId}/summary`)
      ])
      setPatient(pRes.data); setSummary(sRes.data.summary)
    } catch (err) {
      setError(err.response?.data?.message || "Patient not found")
    } finally { setLoading(false) }
  }
  return (
    <div>
      <h2>View Patient</h2>
      <p className="subtitle">Polymorphism in action — summary differs for StudentPatient vs StaffPatient</p>
      <form onSubmit={search}>
        <div className="form-group">
          <label>Patient ID</label>
          <input placeholder="e.g. p1" value={patientId} onChange={e => setPatientId(e.target.value)} required />
        </div>
        <button type="submit">{loading ? "⟳ Searching..." : "⚡ Search Patient"}</button>
      </form>
      {error && <div className="result error">❌ {error}</div>}
      {patient && (
        <div style={{ marginTop:"24px" }}>
          <div className="patient-card">
            <h3>◈ Patient Details</h3>
            <p><strong>ID:</strong> {patient.id}</p>
            <p><strong>Name:</strong> {patient.name}
              <span className={`badge ${patient.type?.toLowerCase().includes("student") ? "student" : "staff"}`}>{patient.type}</span>
            </p>
            <p><strong>Email:</strong> {patient.email}</p>
          </div>
          <div className="summary-card">
            <h3>◈ Polymorphic Summary</h3>
            <p>{summary}</p>
          </div>
        </div>
      )}
    </div>
  )
}

// ═══════════════════════════════════════
// ROLE-BASED DASHBOARDS
// ═══════════════════════════════════════
const ROLE_TABS = {
  Admin: [
    { id: "registerPatient", label: "🏥 Register Patient", component: <RegisterPatient /> },
    { id: "registerStaff",   label: "👨‍⚕️ Register Staff",   component: <RegisterStaff /> },
    { id: "bookAppointment", label: "📅 Book Appointment", component: <BookAppointment /> },
    { id: "addDiagnosis",    label: "🩺 Add Diagnosis",    component: <AddDiagnosis /> },
    { id: "updateVitals",    label: "💉 Update Vitals",    component: <UpdateVitals /> },
    { id: "viewPatient",     label: "🔍 View Patient",     component: <ViewPatient /> },
  ],
  Doctor: [
    { id: "bookAppointment", label: "📅 Book Appointment", component: <BookAppointment /> },
    { id: "addDiagnosis",    label: "🩺 Add Diagnosis",    component: <AddDiagnosis /> },
    { id: "viewPatient",     label: "🔍 View Patient",     component: <ViewPatient /> },
  ],
  Nurse: [
    { id: "bookAppointment", label: "📅 Book Appointment", component: <BookAppointment /> },
    { id: "updateVitals",    label: "💉 Update Vitals",    component: <UpdateVitals /> },
    { id: "viewPatient",     label: "🔍 View Patient",     component: <ViewPatient /> },
  ],
}

const ROLE_COLORS = {
  Admin:  { color: "#7800ff", bg: "rgba(120,0,255,0.1)",   border: "rgba(120,0,255,0.3)" },
  Doctor: { color: "#00d4ff", bg: "rgba(0,212,255,0.1)",   border: "rgba(0,212,255,0.3)" },
  Nurse:  { color: "#00ff88", bg: "rgba(0,255,136,0.1)",   border: "rgba(0,255,136,0.3)" },
}

// ═══════════════════════════════════════
// MAIN APP
// ═══════════════════════════════════════
export default function App() {
  const [user, setUser] = useState(null)
  const [tab, setTab] = useState(null)

  const login = user => {
    setUser(user)
    setTab(ROLE_TABS[user.role][0].id)
  }

  if (!user) return <Login onLogin={login} />

  const tabs = ROLE_TABS[user.role]
  const activeTab = tabs.find(t => t.id === tab)
  const roleStyle = ROLE_COLORS[user.role]

  return (
    <div className="app">
      <header>
        <h1>ZUT MediTrack</h1>
        <p>Zambia University of Technology — Campus Health Clinic Management System</p>
        <div style={{ marginTop:"16px", display:"flex", alignItems:"center", justifyContent:"center", gap:"12px" }}>
          <span style={{ background: roleStyle.bg, color: roleStyle.color, border:`1px solid ${roleStyle.border}`, padding:"6px 16px", borderRadius:"2px", fontSize:"12px", letterSpacing:"1px", textTransform:"uppercase" }}>
            👤 {user.name} — {user.role}
          </span>
          <button onClick={() => setUser(null)} style={{ background:"rgba(255,50,50,0.1)", color:"#ff6b6b", border:"1px solid rgba(255,50,50,0.3)", padding:"6px 16px", borderRadius:"2px", fontSize:"12px", letterSpacing:"1px", cursor:"pointer", textTransform:"uppercase" }}>
            Logout
          </button>
        </div>
      </header>

      <nav>
        {tabs.map(t => (
          <button key={t.id} className={tab === t.id ? "active" : ""} onClick={() => setTab(t.id)}>
            {t.label}
          </button>
        ))}
      </nav>

      <main>
        {activeTab?.component}
      </main>
    </div>
  )
}