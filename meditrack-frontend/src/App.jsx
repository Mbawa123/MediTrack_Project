import { useState } from "react"
import axios from "axios"

const API = "http://localhost:7070"

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
      {result && <pre className={`result ${isError ? "error" : "success"}`}>{isError ? "❌ ERROR\n" : "✅ SUCCESS\n"}{result}</pre>}
    </div>
  )
}

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
      {result && <pre className={`result ${isError ? "error" : "success"}`}>{isError ? "❌ ERROR\n" : "✅ SUCCESS\n"}{result}</pre>}
    </div>
  )
}

function BookAppointment() {
  const [form, setForm] = useState({})
  const [result, setResult] = useState(null)
  const [isError, setIsError] = useState(false)
  const handle = e => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = async e => {
    e.preventDefault()
    try {
      const res = await axios.post(`${API}/appointments/book`, {
        patientId: form.patientId,
        staffId: form.staffId,
        dateTime: form.dateTime + ":00"
      })
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
      {result && <pre className={`result ${isError ? "error" : "success"}`}>{isError ? "❌ ERROR\n" : "✅ SUCCESS\n"}{result}</pre>}
    </div>
  )
}

function AddDiagnosis() {
  const [form, setForm] = useState({})
  const [result, setResult] = useState(null)
  const [isError, setIsError] = useState(false)
  const handle = e => setForm({ ...form, [e.target.name]: e.target.value })
  const submit = async e => {
    e.preventDefault()
    try {
      const res = await axios.post(`${API}/records/${form.patientId}/diagnosis`, {
        staffId: form.staffId,
        description: form.description
      })
      setResult(JSON.stringify(res.data, null, 2)); setIsError(false)
    } catch (err) {
      setResult(JSON.stringify(err.response?.data || err.message, null, 2)); setIsError(true)
    }
  }
  return (
    <div>
      <h2>Add Diagnosis</h2>
      <p className="subtitle">Only Doctors can add diagnoses — try a Nurse ID to see the 403 FORBIDDEN response</p>
      <form onSubmit={submit}>
        <div className="form-row">
          <div className="form-group"><label>Patient ID</label><input name="patientId" placeholder="e.g. p1" onChange={handle} required /></div>
          <div className="form-group"><label>Staff ID (Doctor or Nurse)</label><input name="staffId" placeholder="e.g. d1 or n1" onChange={handle} required /></div>
        </div>
        <div className="form-group">
          <label>Diagnosis Description</label>
          <textarea name="description" rows="4" placeholder="Describe the diagnosis..." onChange={handle} required style={{resize:"vertical", width:"100%", padding:"12px", background:"rgba(0,212,255,0.03)", border:"1px solid rgba(0,212,255,0.2)", borderRadius:"4px", color:"#e0e6ff", fontFamily:"inherit"}} />
        </div>
        <button type="submit">⚡ Submit Diagnosis</button>
      </form>
      {result && <pre className={`result ${isError ? "error" : "success"}`}>{isError ? "❌ ERROR\n" : "✅ SUCCESS\n"}{result}</pre>}
    </div>
  )
}

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
        <div style={{marginTop:"24px"}}>
          <div className="patient-card">
            <h3>◈ Patient Details</h3>
            <p><strong>ID:</strong> {patient.id}</p>
            <p><strong>Name:</strong> {patient.name}
              <span className={`badge ${patient.type?.toLowerCase().includes("student") ? "student" : "staff"}`}>
                {patient.type}
              </span>
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

export default function App() {
  const [tab, setTab] = useState("registerPatient")
  const tabs = [
    { id: "registerPatient", label: "🏥 Register Patient" },
    { id: "registerStaff",   label: "👨‍⚕️ Register Staff" },
    { id: "bookAppointment", label: "📅 Book Appointment" },
    { id: "addDiagnosis",    label: "🩺 Add Diagnosis" },
    { id: "viewPatient",     label: "🔍 View Patient" },
  ]
  return (
    <div className="app">
      <header>
        <h1>ZUT MediTrack</h1>
        <p>Zambia University of Technology — Campus Health Clinic Management System</p>
      </header>
      <nav>
        {tabs.map(t => (
          <button key={t.id} className={tab === t.id ? "active" : ""} onClick={() => setTab(t.id)}>
            {t.label}
          </button>
        ))}
      </nav>
      <main>
        {tab === "registerPatient" && <RegisterPatient />}
        {tab === "registerStaff"   && <RegisterStaff />}
        {tab === "bookAppointment" && <BookAppointment />}
        {tab === "addDiagnosis"    && <AddDiagnosis />}
        {tab === "viewPatient"     && <ViewPatient />}
      </main>
    </div>
  )
}