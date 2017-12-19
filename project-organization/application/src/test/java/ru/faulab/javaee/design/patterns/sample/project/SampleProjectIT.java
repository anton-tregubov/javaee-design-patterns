package ru.faulab.javaee.design.patterns.sample.project;

import com.google.common.base.Preconditions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.faulab.javaee.design.patterns.sample.project.note.Note;
import ru.faulab.javaee.design.patterns.sample.project.note.web.rest.vo.NoteData;
import ru.faulab.javaee.design.patterns.sample.project.platform.expection.ErrorValueObject;
import ru.faulab.javaee.design.patterns.sample.project.platform.impl.JacksonActivator;
import ru.faulab.javaee.design.patterns.sample.project.platform.impl.JacksonConfiguration;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.iterableWithSize;
import static ru.faulab.javaee.design.patterns.sample.project.note.impl.NoteFacadeImpl.DEFAULT_NOTE_LIMIT;

@RunWith(Arquillian.class)
public class SampleProjectIT {
    @Deployment
    public static WebArchive createDeployment() {
        String testingArtifact = System.getProperty("testingArtifact");
        Preconditions.checkNotNull(testingArtifact, "\"testingArtifact\" java property missing");
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(testingArtifact));
    }

    @ArquillianResource
    URI applicationUrl;


    @Test
    @InSequence(1)
    public void empty_notes_at_startup() {
        List<Note> notes = noteRestApi().request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<Note>>() {});
        assertThat(notes, emptyIterable());
    }

    @Test
    @InSequence(2)
    public void add_note() {
        String noteText = "Note Text";
        Note createdNote = noteRestApi()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(NoteData.builder().content(noteText).build()), Note.class);
        assertThat(createdNote.getId(), notNullValue());
        assertThat(createdNote.getContent(), is(noteText));
        assertThat(createdNote.getCreatedWhen().isBefore(LocalDateTime.now().plusSeconds(1)), is(true));
    }

    @Test
    @InSequence(3)
    public void only_one_note_after_add() {
        Collection<Note> notes = noteRestApi()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<Note>>() {});

        assertThat(notes, iterableWithSize(1));
        Note aloneNote = notes.stream().findAny().orElseThrow(AssertionError::new);
        assertThat(aloneNote, notNullValue());

        Note noteByApi = noteRestApi()
                .path(aloneNote.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Note.class);

        assertThat(aloneNote, is(noteByApi));
    }

    @Test
    @InSequence(4)
    public void update_note() {
        Note note = anyNoteRestApi().orElseThrow(AssertionError::new);

        String newContent = "new content";
        Note updatedNote = noteRestApi()
                .path(note.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(NoteData.builder().content(newContent).build()), Note.class);

        assertThat(updatedNote, is(note.withContent(newContent)));
    }

    @Test
    @InSequence(5)
    public void delete_note() {
        Note note = anyNoteRestApi().orElseThrow(AssertionError::new);
        noteRestApi().path(note.getId()).request().delete().getStatus();

        assertThat(anyNoteRestApi().isPresent(), is(false));
    }

    @Test
    public void not_found_note_read_case() {
        assertThat(noteRestApi().path("42").request(MediaType.APPLICATION_JSON_TYPE).get().getStatus(),
                is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void invalid_create_data() {
        Response response = noteRestApi()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity("{}", MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void invalid_update_data() {
        Response response = noteRestApi()
                .path("any")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity("{}", MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    @InSequence(6)
    public void only_100_notes_can_be_created() {
        Function<Integer, Response> call = value -> noteRestApi()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(NoteData.builder().content("Note # " + value).build()));
        IntStream.range(0, DEFAULT_NOTE_LIMIT).forEach(value -> assertThat(call.apply(value).getStatus(), is(Response.Status.OK.getStatusCode())));

        assertThat(call.apply(100500).readEntity(ErrorValueObject.class), is(ErrorValueObject.builder().errorCode(3).userMessage("Note limit reached").build()));
    }

    @Test
    public void no_exception_if_delete_not_existed_note() {
        noteRestApi().path("42").request().delete().getStatus();
    }

    @Test
    public void update_not_existed_note_should_fail() {
        assertThat(noteRestApi()
                        .path("42")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .put(Entity.entity(Note.builder().id("42").content("zlo").createdWhen(LocalDateTime.now()).build(), MediaType.APPLICATION_JSON_TYPE)).getStatus(),
                is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    private Optional<Note> anyNoteRestApi() {
        return noteRestApi()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<Note>>() {
                })
                .stream().findAny();
    }

    private WebTarget noteRestApi() {
        return ClientBuilder.newClient()
                .register(JacksonConfiguration.class)
                .register(JacksonActivator.class)
                .target(applicationUrl).path("rest").path("notes");
    }
}
