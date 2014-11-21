mongo-scrub
===========

### Introduction 
I am hoping to share with you what I have learned in 4-7 days. You should be able to run this on your machine 

### What is the big deal 
Following these instruction will show you how to run 2 different VMs on your machine, one will be production and the other will be a test machine. 
Both are running a mongodb server. In addition, I will show you how to backup one and push a scrubbed copy to the other machine.

### Prerequisites 

* Please [Visit the download page to install Vagrant](https://www.vagrantup.com/downloads.html) and use the appropriate download for your system
* If you are using a Windows machine please know that it does not come with a native ssh client so you will have to 
    * You will need to install [PuTTY](http://the.earth.li/~sgtatham/putty/latest/x86/putty.exe)
    * You will also need to install [PuTTYgen](http://the.earth.li/~sgtatham/putty/latest/x86/puttygen.exe)
* Download [git](http://git-scm.com/downloads)!!
* Once you have downloaded the software above please head to your **terminal**(Unix, Linux, OS X) or the **command prompt**(Windows) and:
* I recommend you run the commands below in your home directory, or directory you have read/write access to.

Downloading the code
--------------------
    $ git clone https://github.com/ramziyassine/mongo-scrub.git
    $ cd mongo-scrub/src/main/resources/vagrant/


### Guided Tour

Follow this steps

Create the VMs
--------------
    $ vagrant up
I do apologize this is my first experience with vagrant and I could have create a self box but instead I have a bootstrap provisioning and it will take a while until both VMs are up.
This is a good time for a ping pong game (maybe one set) or cup of coffee!!

### What happened

 Two virtual machines have been created for you and they are ready to roll
 | Name | IP address  | Mongodb server port    |
 | ---- | ----------  | ---------------------- |
 | prod | 192.168.4.15| 4815                   |
 | test | 192.168.4.16| 4816                   |


Let us Populate the Production database with some data
--------------
    $ vagrant ssh prod
This step on Windows will need to put done on putty. [Please check this good resource to see how](http://stackoverflow.com/questions/9885108/ssh-to-vagrant-box-in-windows)
You can still run the command above in the prompt and vagrant will tell you what to do.

Generate
--------------
    vagrant@prod:~$ /vagrant/generateData.sh
You can execute the bash script above on the production server and it will populate the database with 100 users

Take a peek
--------------
 






GitHub Markup
=============

We use this library on GitHub when rendering your README or any other
rich text file. The generated HTML is then run through filters in the [html-pipeline](https://github.com/jch/html-pipeline) to perform things like [sanitization](#html-sanitization) and [syntax highlighting](https://github.com/jch/html-pipeline/blob/master/lib/html/pipeline/syntax_highlight_filter.rb).

Markups
-------

The following markups are supported.  The dependencies listed are required if
you wish to run the library. You can also run `script/bootstrap` to fetch them all.

* [.markdown, .mdown, .mkdn, .md](http://daringfireball.net/projects/markdown/) -- `gem install redcarpet` (https://github.com/vmg/redcarpet)
* [.textile](http://www.textism.com/tools/textile/) -- `gem install RedCloth`
* [.rdoc](http://rdoc.sourceforge.net/) -- `gem install rdoc -v 3.6.1`
* [.org](http://orgmode.org/) -- `gem install org-ruby`
* [.creole](http://wikicreole.org/) -- `gem install creole`
* [.mediawiki, .wiki](http://www.mediawiki.org/wiki/Help:Formatting) -- `gem install wikicloth`
* [.rst](http://docutils.sourceforge.net/rst.html) -- `easy_install docutils`
* [.asciidoc, .adoc, .asc](http://asciidoc.org/) -- `gem install asciidoctor` (http://asciidoctor.org)
* [.pod](http://search.cpan.org/dist/perl/pod/perlpod.pod) -- `Pod::Simple::HTML`
  comes with Perl >= 5.10. Lower versions should install Pod::Simple from CPAN.

Installation
-----------

    gem install github-markup

Usage
-----

    require 'github/markup'
    GitHub::Markup.render('README.markdown', "* One\n* Two")

Or, more realistically:

    require 'github/markup'
    GitHub::Markup.render(file, File.read(file))

Contributing
------------

See [Contributing](CONTRIBUTING.md)

HTML sanitization
-----------------

HTML rendered by the various markup language processors gets passed through an [HTML sanitization filter](https://github.com/jch/html-pipeline/blob/master/lib/html/pipeline/sanitization_filter.rb) for security reasons. HTML elements not in the whitelist are removed. HTML attributes not in the whitelist are removed from the preserved elements.

The following HTML elements, organized by category, are whitelisted:

|Type | Elements
|------|----------
|Headings | `h1`, `h2`, `h3`, `h4`, `h5`, `h6`, `h7`, `h8`
|Prose |  `p`, `div`, `blockquote`
|Formatted | `pre`
| Inline | `b`, `i`, `strong`, `em`, `tt`, `code`, `ins`, `del`, `sup`, `sub`, `kbd`, `samp`, `q`, `var`
| Lists | `ol`, `ul`, `li`, `dl`, `dt`, `dd`
| Tables | `table`, `thead`, `tbody`, `tfoot`, `tr`, `td`, `th`
| Breaks | `br`, `hr`
| Ruby (East Asian) | `ruby`, `rt`, `rp`

The following attributes, organized by element, are whitelisted:

|Element | Attributes
|------|----------
| `a` | `href` (`http://`, `https://`, `mailto://`, `github-windows://`, and `github-mac://` URI schemes and relative paths only)
| `img` | `src` (`http://` and `https://` URI schemes and relative paths only)
| `div` | `itemscope`, `itemtype`
| All | `abbr`, `accept`, `accept-charset`, `accesskey`, `action`, `align`, `alt`, `axis`, `border`, `cellpadding`, `cellspacing`, `char`, `charoff`, `charset`, `checked`, `cite`, `clear`, `cols`, `colspan`, `color`, `compact`, `coords`, `datetime`, `dir`, `disabled`, `enctype`, `for`, `frame`, `headers`, `height`, `hreflang`, `hspace`, `ismap`, `label`, `lang`, `longdesc`, `maxlength`, `media`, `method`, `multiple`, `name`, `nohref`, `noshade`, `nowrap`, `prompt`, `readonly`, `rel`, `rev`, `rows`, `rowspan`, `rules`, `scope`, `selected`, `shape`, `size`, `span`, `start`, `summary`, `tabindex`, `target`, `title`, `type`, `usemap`, `valign`, `value`, `vspace`, `width`, `itemprop`

Note that the `id` attribute is *not* whitelisted.