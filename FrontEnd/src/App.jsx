import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { CssBaseline, Container, Box, ThemeProvider, createTheme } from '@mui/material'
import NavBar from './components/NavBar'
import Login from './pages/Login'
import CustomerSignup from './pages/CustomerSignup'
import DriverSignup from './pages/DriverSignup'
import AdminDashboard from './pages/AdminDashboard'
import Cabs from './pages/Cabs'
import Booking from './pages/Booking'
import CustomerDashboard from './pages/CustomerDashboard'
import DriverDashboard from './pages/DriverDashboard'
import Home from './pages/Home'

const theme = createTheme({
  palette: {
    primary: { main: '#1e88e5' },
    secondary: { main: '#fbc02d' },
    background: { default: '#f4f7fc', paper: '#ffffff' }
  },
  typography: {
    fontFamily: ['Inter', 'Roboto', 'sans-serif'].join(','),
  },
  shape: {
    borderRadius: 16,
  },
})

function App(){
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <NavBar />
      <Box sx={{ py: 4, backgroundColor: 'background.default', minHeight: '100vh' }}>
        <Container maxWidth="lg">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/home" element={<Home />} />
            <Route path="/login" element={<Login/>} />
            <Route path="/signup/customer" element={<CustomerSignup/>} />
            <Route path="/signup/driver" element={<DriverSignup/>} />
            <Route path="/admin" element={<AdminDashboard/>} />
            <Route path="/cabs" element={<Cabs/>} />
            <Route path="/book" element={<Booking/>} />
            <Route path="/customer" element={<CustomerDashboard/>} />
            <Route path="/driver" element={<DriverDashboard/>} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Container>
      </Box>
    </ThemeProvider>
  )
}

export default App
