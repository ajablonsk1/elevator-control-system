import React from 'react';
import {Container, Navbar, Card, Row, Button, NavLink} from "react-bootstrap";
import {GrElevator} from "react-icons/gr"

const Home = () => {
    return (
        <>
            <Navbar bg="light">
                <Container>
                    <Navbar.Brand>
                        <GrElevator/>
                        <span style={{marginLeft: "10px"}}>Elevator system</span>
                    </Navbar.Brand>
                </Container>
            </Navbar>
            <Container style={{marginTop: "100px"}}>
                <Row className="justify-content-md-center">
                    <Card style={{ width: '30rem' }}>
                        <Card.Body style={{ marginLeft: "auto", marginRight:"auto" }}>
                            <Card.Title>
                                Click here to start simulation
                            </Card.Title>
                        </Card.Body>
                        <NavLink  href="/simulation"
                                  style={{ width:'12rem', marginLeft: "auto", marginRight:"auto",marginBottom:"20px" }}>
                            <Button variant="light">Start simulation!</Button>
                        </NavLink>
                    </Card>
                </Row>
            </Container>
        </>
    );
};

export default Home;