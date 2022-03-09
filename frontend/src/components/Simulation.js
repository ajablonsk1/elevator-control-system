import React, {useDebugValue, useEffect} from 'react';
import {useState} from 'react';
import Api from './Api'
import {
    Button,
    Container,
    NavLink,
    Card,
    Row,
    Form,
    Navbar,
    Nav,
    FormSelect,
} from "react-bootstrap";

const Simulation = () => {
    const floors = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    const [elevators, setElevators] = useState([])
    const [orderedFloor, setOrderedFloor] = useState('')
    const [direction, setDirection] = useState('')
    const [orderedFloorInElevator, setOrderedFloorInElevator] = useState('')
    const [id, setId] = useState('')

    const getElevatorsFromDb = () => {
        Api.get("api/v1/elevators"
        ).then(res => {
            setElevators(res.data)
        }).catch(e => {
            console.log(e)
        })
    }

    const onOrderSubmit = (e) => {
        e.preventDefault()
        Api.post("/api/v1/order", {
            floor: orderedFloor,
            direction: direction
        }).then(res => {
            console.log(res.data)
        }).catch(e => {
            console.log(e)
        })
    }

    const onPerformStepClick = (e) => {
        e.preventDefault()
        Api.get("/api/v1/step"
        ).then(res => {
            setElevators(res.data)
        }).catch(e => {
            console.log(e)
        })
    }

    const onOrderInElevator = (e) => {
        e.preventDefault()
        Api.post("/api/v1/order/floor", {
            id: id,
            floor: orderedFloorInElevator
        }).then(res => {
            console.log(res.data)
        }).catch(e => {
            console.log(e)
        })
    }

    const onDeleteClick = (e) => {
        e.preventDefault()
        Api.delete("/api/v1/elevator",
            {params: {id: e.target.value}
        }).then(res => {
            console.log(res.data)
            getElevatorsFromDb()
        }).catch(e => {
            console.log(e)
        })
    }

    useEffect(() => {
        getElevatorsFromDb()
        setOrderedFloor('0')
        setDirection('Up')
        setOrderedFloorInElevator('0')
    }, [])

    return (
        <div>
            <Navbar bg="light" expand="lg">
                <Container fluid>
                    <Navbar.Brand href="/">Elevator system</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <NavLink href="/addElevator">
                                Add Elevator
                            </NavLink>
                            <NavLink href="/update">Update specific elevator</NavLink>
                            <Button variant="outline-primary" onClick={onPerformStepClick}>Perform step</Button>
                        </Nav>
                        <Form className="d-flex"
                              onSubmit={onOrderSubmit}
                              id="form">
                            <FormSelect
                                placeholder="Select floor"
                                className="me-2"
                                value={orderedFloor}
                                onChange={(e) => setOrderedFloor(e.target.value)}
                            >
                                    {floors.map((floor, index) => (
                                        <option key={index}>{floor}</option>
                                    ))}
                            </FormSelect>
                            <FormSelect
                                style={{width: "200%"}}
                                placeholder="Select floor"
                                className="me-2"
                                value={direction}
                                onChange={(e) => setDirection(e.target.value)}>
                                <option>Up</option>
                                <option>Down</option>
                            </FormSelect>
                            <Button variant="outline-primary" type="submit">Order</Button>
                        </Form>
                    </Navbar.Collapse>
            </Container>
        </Navbar>
    <Container style={{marginTop: "20px"}}>
        <Row xs={1} md={4} className="justify-content-md-center">
            {elevators.map((elevator, index) => (
                <Card key={index} style={{width: '18rem', margin: "10px"}}>
                    <Card.Body>
                        <Card.Title>Elevator</Card.Title>
                        <Card.Subtitle className="mb-2 text-muted">ID: {elevator.id}</Card.Subtitle>
                        <Card.Text>Current floor: {elevator.currentFloor}</Card.Text>
                        <Card.Text>Destination floor: {elevator.destinationFloor}</Card.Text>
                        <Card.Text>Floors to stop:
                            {elevator.floors.length > 0 ?
                                <>{elevator.floors.map((floor, index) => (
                                    <span key={index}>{floor}; </span>
                                ))}</> :
                                <span> None</span>
                            }
                        </Card.Text>
                        <Card.Text>
                            <Button value={elevator.id} variant="outline-danger" onClick={onDeleteClick}>Delete</Button>
                        </Card.Text>
                        <Form className="d-flex"
                              onSubmit={onOrderInElevator}
                              id="form">
                            <FormSelect
                                placeholder="Select floor"
                                className="me-2"
                                value={orderedFloorInElevator}
                                onChange={(e) => setOrderedFloorInElevator(e.target.value)}
                            >
                                {floors.map((floor, index) => (
                                    <option key={index}>{floor}</option>
                                ))}
                            </FormSelect>
                            <Button onClick={(e) => setId(elevator.id)} variant="outline-primary" type="submit">Order</Button>
                        </Form>
                    </Card.Body>
                </Card>
            ))}
        </Row>
    </Container>
</div>
)
    ;
};

export default Simulation;