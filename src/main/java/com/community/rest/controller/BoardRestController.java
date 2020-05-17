package com.community.rest.controller;

import com.community.rest.domain.Board;
import com.community.rest.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/boards")
public class BoardRestController {

    @Autowired
    private BoardRepository boardRepository;

    //page
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBoards(@PageableDefault Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pageable.getPageSize(), boards.getNumber(), boards.getTotalElements());
        PagedModel<Board> resources = new PagedModel<>(boards.getContent(), pageMetadata);
        resources.add(linkTo(methodOn(BoardRestController.class).getBoards(pageable)).withSelfRel());
        return ResponseEntity.ok(resources);

    }

    //insert
    @PostMapping
    public ResponseEntity<?> postBoard(@RequestBody Board board){

        board.setCreatedDateNow(); //insert Time
        board.setupdatedDateNow(); //update Time

        boardRepository.save(board);

        System.out.println("Insert idx == " + board.getIdx());
        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

    //modify
    @PutMapping("/{idx}")
    public ResponseEntity<?> putBoard(@PathVariable("idx")Long idx, @RequestBody Board board){
        System.out.println("Modify idx == " + idx);

        Board persistBoard = boardRepository.getOne(idx);
        persistBoard.update(board);
        boardRepository.save(persistBoard);
        return new ResponseEntity<>("{}",HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteBoard(@PathVariable("idx")Long idx) {
        System.out.println("Delete idx == " + idx);
        boardRepository.deleteById(idx);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}

