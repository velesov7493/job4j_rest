package ru.job4j.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.domains.Person;
import ru.job4j.auth.security.JwtTokenProvider;
import ru.job4j.auth.services.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService persons;
    private final JwtTokenProvider provider;

    public PersonController(PersonService persons, JwtTokenProvider provider) {
        this.persons = persons;
        this.provider = provider;
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> findAll() {
        List<Person> records = persons.findAll();
        return
                records.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Person person = persons.findById(id);
        return
                person == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        Person result = persons.save(person);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/by-token")
    public ResponseEntity<Person> validate(HttpServletRequest request) {
        String token = provider.resolveToken(request);
        Person result = provider.getUser(token);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        Person result = persons.save(person);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        persons.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
